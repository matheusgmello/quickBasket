import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  
  // Only add auth header for requests to /basket
  if (req.url.includes('/basket')) {
    req = req.clone({
      setHeaders: {
        Authorization: `Basic ${authService.credentials}`
      }
    });
  }
  
  return next(req);
};
