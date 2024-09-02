import { CanActivateFn, Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { AuthService } from '../routes/session-management/auth.service';
import { inject } from '@angular/core';

export const GuestGuard: CanActivateFn = (): Observable<boolean> => {

  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.currentUser$.pipe(
    map(isAuthenticate => {
      if (isAuthenticate) {
        router.navigate(['/']);  // Redirect to a protected route if already authenticated
        return false;
      }
      return true;
    })
  )
};
