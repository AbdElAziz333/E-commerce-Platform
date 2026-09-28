export interface SignupRequest {
    firstName: string
    lastName: string
    email: string
    password: string
    phoneNumber: string
}

export interface OtpRequest {
    verificationId: string
    email: string
    otp: string
}

export interface OtpData {
    verificationId: string
    email: string
}