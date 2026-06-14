import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpHandlerFn
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const jwtInterceptor: HttpInterceptorFn = (request: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  const token = localStorage.getItem('auth_token');
  const isApiUrl = request.url.startsWith('http://localhost:8080') || 
                   request.url.startsWith('https://api.learningplatform.com');

  if (token && isApiUrl) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request);
}

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Add auth header with jwt if user is logged in and request is to the api url
    const currentUser = this.authService.currentUserValue;
    const isApiUrl = request.url.startsWith('http://localhost:8080') || 
                     request.url.startsWith('https://api.learningplatform.com');

    if (currentUser && currentUser.token && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401 && !this.isRefreshing) {
          this.isRefreshing = true;
          return this.authService.refreshToken().pipe(
            switchMap(() => {
              this.isRefreshing = false;
              const updatedUser = this.authService.currentUserValue;
              const clonedRequest = request.clone({
                setHeaders: {
                  Authorization: `Bearer ${updatedUser.token}`
                }
              });
              return next.handle(clonedRequest);
            }),
            catchError(err => {
              this.isRefreshing = false;
              this.authService.logout();
              return throwError(() => err);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
}
