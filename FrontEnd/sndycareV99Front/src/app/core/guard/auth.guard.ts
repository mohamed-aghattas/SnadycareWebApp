import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService), router = inject(Router);
  if (auth.isLoggedInSnapshot()) return true;
  router.navigate(['/login']); return false;
};
