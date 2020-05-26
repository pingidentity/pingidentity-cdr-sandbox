import { Component, OnDestroy, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Subscription } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { LeftSidebarService } from '@app/core/services/left-sidebar.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

    breadcrumbSubscription: Subscription;

    breadcrumbItems: MenuItem[];

    constructor(
        private breadcrumbService: BreadcrumbService,
        private oauthService: OAuthService,
        private leftSidebar: LeftSidebarService
    ) {}

    ngOnInit() {
        this.breadcrumbSubscription = this.breadcrumbService.itemsHandler
            .subscribe(response => this.breadcrumbItems = response);

    }

    ngOnDestroy() {
        if (this.breadcrumbSubscription) {
            this.breadcrumbSubscription.unsubscribe();
        }
    }

    logout() {
        this.oauthService.logOut(false);
    }

    toggleBurger() {
        this.leftSidebar.toggleBurgers.emit();
    }
}
