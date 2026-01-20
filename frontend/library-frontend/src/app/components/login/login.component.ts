import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.models';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    loginData: LoginRequest = {
        email: '',
        password: ''
    };

    isLoading = false;
    errorMessage = '';
    showPassword = false;

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    togglePasswordVisibility(): void {
        this.showPassword = !this.showPassword;
    }

    onSubmit(): void {
        if (!this.loginData.email || !this.loginData.password) {
            this.errorMessage = 'Please fill in all fields';
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';

        this.authService.login(this.loginData).subscribe({
            next: (response) => {
                this.isLoading = false;
                console.log('Login successful:', response);
                this.router.navigate(['/home']);
            },
            error: (error) => {
                this.isLoading = false;
                console.error('Login error:', error);
                this.errorMessage = error.error || 'Login failed. Please check your credentials.';
            }
        });
    }
}
