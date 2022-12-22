import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

// aplicação
import { LoginService } from '../services/login.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(private _loginService: LoginService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this._loginService.isAuthenticated) {
            req = req.clone({ setHeaders: { Authorization: 'Bearer ' + this._loginService.authInfo ?? '' } });
        }
        return next.handle(req).pipe(catchError(error => this.handleError(error)));
    }

    private handleError(error: HttpResponse<any>) {
        if (error.status === 401) {
            this._loginService.logout();
        }
        return throwError(error);
    }
}