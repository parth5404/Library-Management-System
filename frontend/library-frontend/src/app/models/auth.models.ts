export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    jwString: string;
}

export interface SignupRequest {
    customerName: string;
    email: string;
    countryCode: string;
    mobileNumber: string;
    address: string;
    addressLine1: string;
    addressLine2: string;
    city: string;
    state: string;
    pinCode: string;
    dateOfBirth: string;
    password: string;
    confirmPassword: string;
    secretQuestion: string;
    secretAnswer: string;
}

export interface SignupResponse {
    client_id: string;
}
