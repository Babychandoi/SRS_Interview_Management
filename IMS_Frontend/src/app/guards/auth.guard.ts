import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../routes/session-management/auth.service';
import { map, Observable } from 'rxjs';

export const AuthGuard: CanActivateFn = (): Observable<boolean> => {

  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.currentUser$.pipe(
    map(isAuthenticated => {
      if (!isAuthenticated) {
        router.navigate(['/login']);
        return false;
      }
      return true;
    })
  )
}
