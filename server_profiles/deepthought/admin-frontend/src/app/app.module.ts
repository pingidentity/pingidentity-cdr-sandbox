import {APP_INITIALIZER, NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LocationStrategy, HashLocationStrategy, PercentPipe, CurrencyPipe, APP_BASE_HREF} from '@angular/common';
import {OAuthModule} from 'angular-oauth2-oidc';

import {environment} from '@env';
import {AppRoutes} from './app.routes';
import {AppComponent} from './app.component';

import {AuthGuardService} from '@app/core/guards/auth-guard.service';

import {PageNotFoundComponent} from './pages/not-found/page.not-found.component';
import {PageErrorComponent} from './pages/error/page.error.component';
import {PageAccessDeniedComponent} from './pages/access-denied/page.access-denied.component';
import {AuthLoginComponent} from './pages/auth/login/auth.login.component';

import {ProgressSpinnerModule} from 'primeng/primeng';
import {DialogService, MessageService} from 'primeng/api';
import {DynamicDialogModule} from 'primeng/dynamicdialog';

import {SharedModule} from '@app/shared/shared.module';
import {LayoutModule} from './layout/layout.module';
import {ApiModule, BASE_PATH, BrandAdminService, Configuration} from '@bizaoss/deepthought-admin-angular-client';
import {AuthInterceptor} from '@app/core/interceptors/auth.interceptor';
import {initConfig, RuntimeConfigLoaderModule, RuntimeConfigLoaderService} from 'runtime-config-loader';

export function apiConfigFactory(): Configuration {
    return new Configuration({
        accessToken: () => sessionStorage.getItem('access_token')
    });
}

@NgModule({
    imports: [
        RuntimeConfigLoaderModule,
        BrowserModule,
        FormsModule,
        AppRoutes,
        HttpClientModule,
        BrowserAnimationsModule,
        DynamicDialogModule,
        ProgressSpinnerModule,
        OAuthModule.forRoot({
            resourceServer: {
                sendAccessToken: true
            }
        }),
        SharedModule,
        LayoutModule,
        ApiModule.forRoot(apiConfigFactory)
    ],
    declarations: [
        AppComponent,
        PageNotFoundComponent,
        PageErrorComponent,
        PageAccessDeniedComponent,
        AuthLoginComponent,
    ],
    providers: [
        {
            provide: APP_INITIALIZER,
            useFactory: initConfig,
            deps: [RuntimeConfigLoaderService],
            multi: true
        },
        {
            provide: BASE_PATH,
            useFactory: (configService: RuntimeConfigLoaderService) => configService.getConfigObjectKey("API_BASE_PATH"),
            deps: [RuntimeConfigLoaderService],
            multi: true
        },
        {
            provide: LocationStrategy,
            useClass: HashLocationStrategy
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        BrandAdminService,
        AuthGuardService,
        MessageService,
        PercentPipe,
        CurrencyPipe,
        DialogService,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
