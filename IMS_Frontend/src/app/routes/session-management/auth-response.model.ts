export interface AuthResponse {
    access_token: string,
    access_token_expiry: number,
    token_type: string,
    full_name: string,
    email: string,
    role: string
}
