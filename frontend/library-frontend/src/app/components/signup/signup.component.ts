import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SignupRequest } from '../../models/auth.models';

@Component({
    selector: 'app-signup',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './signup.component.html',
    styleUrl: './signup.component.css'
})
export class SignupComponent {
    signupData: SignupRequest = {
        customerName: '',
        email: '',
        countryCode: '+91',
        mobileNumber: '',
        address: '',
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        pinCode: '',
        dateOfBirth: '',
        password: '',
        confirmPassword: '',
        secretQuestion: '',
        secretAnswer: ''
    };

    isLoading = false;
    errorMessage = '';
    successMessage = '';
    showPassword = false;
    showConfirmPassword = false;
    fieldErrors: { [key: string]: string } = {};

    secretQuestions = [
        { value: 'pet', label: 'What is your pet\'s name?' },
        { value: 'school', label: 'What is your school name?' },
        { value: 'city', label: 'In which city were you born?' },
        { value: 'book', label: 'What is your favorite book?' }
    ];

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    togglePasswordVisibility(field: 'password' | 'confirm'): void {
        if (field === 'password') {
            this.showPassword = !this.showPassword;
        } else {
            this.showConfirmPassword = !this.showConfirmPassword;
        }
    }

    getFieldError(field: string): string {
        return this.fieldErrors[field] || '';
    }

    hasFieldError(field: string): boolean {
        return !!this.fieldErrors[field];
    }

    validateForm(): boolean {
        this.fieldErrors = {};

        if (!this.signupData.customerName || !this.signupData.email ||
            !this.signupData.mobileNumber || !this.signupData.password ||
            !this.signupData.confirmPassword || !this.signupData.dateOfBirth ||
            !this.signupData.secretQuestion || !this.signupData.secretAnswer) {
            this.errorMessage = 'Please fill in all required fields';
            return false;
        }

        // Validate name (min 3 chars, letters only)
        if (this.signupData.customerName.length < 3) {
            this.fieldErrors['customerName'] = 'Name must be at least 3 characters';
        }

        // Validate country code format (+XX or +XXX)
        const countryCodeRegex = /^\+[1-9]\d{0,3}$/;
        if (!countryCodeRegex.test(this.signupData.countryCode)) {
            this.fieldErrors['countryCode'] = 'Country code must start with + (e.g., +91)';
        }

        // Validate mobile (8-10 digits)
        const mobileRegex = /^\d{8,10}$/;
        if (!mobileRegex.test(this.signupData.mobileNumber)) {
            this.fieldErrors['mobileNumber'] = 'Mobile must be 8-10 digits';
        }

        // Validate address (min 10 chars if provided)
        if (this.signupData.address && this.signupData.address.length < 10) {
            this.fieldErrors['address'] = 'Address must be at least 10 characters';
        }

        // Validate password (min 8 chars, uppercase, lowercase, digit, special char)
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
        if (!passwordRegex.test(this.signupData.password)) {
            this.fieldErrors['password'] = 'Password must be 8+ chars with uppercase, lowercase, number & special character';
        }

        if (this.signupData.password !== this.signupData.confirmPassword) {
            this.fieldErrors['confirmPassword'] = 'Passwords do not match';
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(this.signupData.email)) {
            this.fieldErrors['email'] = 'Please enter a valid email address';
        }

        // Check if any field errors
        if (Object.keys(this.fieldErrors).length > 0) {
            this.errorMessage = 'Please fix the highlighted errors';
            return false;
        }

        return true;
    }

    onSubmit(): void {
        this.errorMessage = '';
        this.successMessage = '';
        this.fieldErrors = {};

        if (!this.validateForm()) {
            return;
        }

        this.isLoading = true;

        this.authService.signup(this.signupData).subscribe({
            next: (response) => {
                this.isLoading = false;
                this.successMessage = `Account created successfully! Your Client ID: ${response.client_id}`;
                setTimeout(() => {
                    this.router.navigate(['/login']);
                }, 2000);
            },
            error: (error) => {
                this.isLoading = false;
                console.error('Signup error:', error);

                // Handle structured validation errors from backend
                if (error.error && error.error.fieldErrors) {
                    this.fieldErrors = error.error.fieldErrors;
                    this.errorMessage = error.error.message || 'Validation failed. Please check the highlighted fields.';
                } else if (error.error && error.error.message) {
                    this.errorMessage = error.error.message;
                } else {
                    this.errorMessage = 'Signup failed. Please try again.';
                }
            }
        });
    }
}
