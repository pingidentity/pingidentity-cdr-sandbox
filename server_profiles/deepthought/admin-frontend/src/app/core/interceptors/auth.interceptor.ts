import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Configuration } from '@bizaoss/deepthought-admin-angular-client';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private configuration: Configuration) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const accessToken = typeof this.configuration.accessToken === 'function'
            ? this.configuration.accessToken()
            : this.configuration.accessToken;

        if (accessToken) {
            const headers = new HttpHeaders().set('Authorization', `Bearer ${accessToken}`);
            const requestWithAuth = request.clone({ headers });
            return next.handle(requestWithAuth);
        }

        return next.handle(request);
    }
}
