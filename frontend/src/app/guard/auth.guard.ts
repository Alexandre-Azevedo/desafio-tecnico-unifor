import { Injectable } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const AuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    router.navigate(['/']);
    return false;
  }

  const expectedRoles = route.data['roles'] as string[] | undefined;
  if (expectedRoles && expectedRoles.length > 0) {
    const userRoles = auth.getUserRoles(); // Implemente esse método no AuthService
    const hasRole = expectedRoles.some(role => userRoles.includes(role));
    if (!hasRole) {
      router.navigate(['/']);
      return false;
    }
  }

  return true;
};
