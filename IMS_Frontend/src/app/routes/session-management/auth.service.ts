import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthResponse } from './auth-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.apiUrl + 'auth';
  private logoutUrl = 'http://localhost:8080/logout';

  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    if (this.currentUser != null) {
      this.setCurrentUser(this.currentUser);
    }
  }

  private setCurrentUser(user: AuthResponse) {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  get currentUser() {
    if (!localStorage.getItem('user')) {
      return null;
    }
    return JSON.parse(localStorage.getItem('user')!);
  }

  login(username: string, password: string) {
    const basicAuthKey = 'Basic ' + btoa(`${username}:${password}`);
    const headers = new HttpHeaders({ 'Authorization': basicAuthKey });
    return this.http.post<AuthResponse>(`${this.baseUrl}/sign-in`, {}, { headers }).pipe(
      map((response: AuthResponse) => {
        if (response) {
          console.log("Login service");
          console.log(response);
          this.setCurrentUser(response);
        }
      })
    );
  }

  logout() {
    return this.http.post(this.logoutUrl, {});
  }

  clearUser() {
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  refreshToken() {
    return this.http.post<AuthResponse>(`${this.baseUrl}/refresh-token`, null).pipe(
      map((response: AuthResponse) => {
        if (response && response.access_token) {
          this.setCurrentUser(response);
        }
        return response;
      }),
      catchError(error => {
        this.clearUser();
        return throwError(error);
      })
    )
  }

  getAccessToken(): string | null {
    const user = this.currentUser;
    return user ? user.accessToken : null;
  }
}
