import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, SignupRequest, SignupResponse } from '../models/auth.models';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API_URL = 'http://localhost:8080/api/v1/auth';
    private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());

    isLoggedIn$ = this.isLoggedInSubject.asObservable();

    constructor(private http: HttpClient) { }

    private hasToken(): boolean {
        return !!localStorage.getItem('jwt_token');
    }

    login(credentials: LoginRequest): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.API_URL}/login`, credentials).pipe(
            tap(response => {
                if (response.jwString) {
                    localStorage.setItem('jwt_token', response.jwString);
                    this.isLoggedInSubject.next(true);
                }
            })
        );
    }

    signup(userData: SignupRequest): Observable<SignupResponse> {
        return this.http.post<SignupResponse>(`${this.API_URL}/signup`, userData);
    }

    logout(): void {
        localStorage.removeItem('jwt_token');
        this.isLoggedInSubject.next(false);
    }

    getToken(): string | null {
        return localStorage.getItem('jwt_token');
    }

    isAuthenticated(): boolean {
        return this.hasToken();
    }
}
