import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError, Observable, switchMap, take, throwError } from 'rxjs';
import { AuthService } from '../routes/session-management/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.authService.currentUser$.pipe(take(1)).subscribe({
      next: (user) => {
        if (user) {
          request = request.clone({
            setHeaders: {
              Authorization: `Bearer ${user.access_token}`
            }
          });
          // console.log(user.access_token);
        }
      },
    });
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 406) {
          return this.authService.refreshToken().pipe(
            switchMap(() => {
              const newAuthToken = this.authService.getAccessToken();
              const newAuthReq = request.clone({
                setHeaders: {
                  Authorization: `Bearer ${newAuthToken}`
                }
              });
              return next.handle(newAuthReq);
            }),
            catchError(err => {
              this.authService.logout();
              return throwError(err);
            })
          );
        } else {
          return throwError(error);
        }
      })
    );
  }
}
