import { Component, OnInit } from '@angular/core';
import { AuthConfig, JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { NavigationEnd, Router } from '@angular/router';
import { environment } from '@env';
import { filter } from 'rxjs/operators';
import { TypeManagementService } from '@app/core/services/type-management.service';
import {BASE_PATH, FormFieldType} from '@bizaoss/deepthought-admin-angular-client';
import {RuntimeConfigLoaderService} from 'runtime-config-loader';

export const STORAGE_REDIRECT_URI_KEY = 'redirectUri';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

    public previousUrl: string;

    constructor(
        private configService: RuntimeConfigLoaderService,
        private oauthService: OAuthService,
        private router: Router,
        private typeManager: TypeManagementService
    ) {}

    ngOnInit() {
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe((e: any) => this.previousUrl = e.url);

        this.saveRedirectPage();
        this.configureAuth();
    }

    private configureAuth() {
        const authConfig: AuthConfig = {
            // Url of the Identity Provider
            issuer: this.configService.getConfigObjectKey('OAUTH2_ISSUER'),
            // URL of the SPA to redirect the user to after login
            redirectUri: window.location.origin,
            // The SPA's id. The SPA is registered with this id at the auth-server
            clientId: this.configService.getConfigObjectKey('OAUTH2_CLIENT_ID'),
            // set the scope for the permissions the client should request
            // The first three are defined by OIDC. The 4th is a usecase-specific one
            scope: this.configService.getConfigObjectKey('OAUTH2_SCOPE'),
            // code based response type
            // responseType: 'code',
            requestAccessToken: true,
            // show debug
            showDebugInformation: !environment.production,
            // oidc enabled
            oidc: true,
            // at hash check disabled... :-(
            disableAtHashCheck: true,
        };

        this.oauthService.configure(authConfig);
        // this.oauthService.setupAutomaticSilentRefresh();
        this.oauthService.tokenValidationHandler = new JwksValidationHandler();
        this.oauthService.loadDiscoveryDocumentAndLogin().then((isLoggedIn) => {
            if (!isLoggedIn) {
                return;
            }

            // this.router.navigate(['/']);

            this.populateTypes();
            this.redirectAfterAuth();
        });
    }

    populateTypes() {
        this.typeManager.populateTypes([
            FormFieldType.BANKINGPRODUCTCATEGORY,
            FormFieldType.BANKINGPRODUCTFEATURETYPE,
            FormFieldType.BANKINGPRODUCTCONSTRAINTTYPE,
            FormFieldType.BANKINGPRODUCTELIGIBILITYTYPE,
            FormFieldType.BANKINGPRODUCTFEETYPE,
            FormFieldType.BANKINGPRODUCTDEPOSITRATETYPE,
            FormFieldType.BANKINGPRODUCTLENDINGRATETYPE,
            FormFieldType.BANKINGPRODUCTLENDINGRATEINTERESTPAYMENTTYPE,
            FormFieldType.COMMONUNITOFMEASURETYPE,
            FormFieldType.BANKINGPRODUCTRATETIERAPPLICATIONMETHOD,
            FormFieldType.BANKINGPRODUCTDISCOUNTTYPE,
            FormFieldType.BANKINGPRODUCTDISCOUNTELIGIBILITYTYPE,
        ]);
    }

    saveRedirectPage() {
        const redirectUri = location.hash.slice(1);
        if (!localStorage.getItem(STORAGE_REDIRECT_URI_KEY) && !redirectUri.startsWith('state=')) {
            localStorage.setItem(STORAGE_REDIRECT_URI_KEY, redirectUri);
        }
    }

    redirectAfterAuth() {
        const redirectUri = localStorage.getItem(STORAGE_REDIRECT_URI_KEY) || '/';
        localStorage.removeItem(STORAGE_REDIRECT_URI_KEY);

        setTimeout(() => this.router.navigate([redirectUri]), 500);
    }
}
