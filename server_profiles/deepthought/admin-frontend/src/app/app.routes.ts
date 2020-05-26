import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { AuthGuardService } from '@app/core/guards/auth-guard.service';
import { LayoutComponent } from './layout/layout.component';

import { PageNotFoundComponent } from './pages/not-found/page.not-found.component';
import { PageErrorComponent } from './pages/error/page.error.component';
import { PageAccessDeniedComponent } from './pages/access-denied/page.access-denied.component';
import { AuthLoginComponent } from './pages/auth/login/auth.login.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        canActivate: [AuthGuardService],
        children: [
            {
                path: '',
                redirectTo: 'brands',
                pathMatch: 'full'
            },
            {
                path: 'brands',
                loadChildren: './modules/brands/brands.module#BrandsModule'
            },
        ]
    },
    {path: 'access-denied', component: PageAccessDeniedComponent},
    {path: 'error', component: PageErrorComponent},
    {path: '404', component: PageNotFoundComponent},
    {path: 'login', component: AuthLoginComponent},
    {path: '**', redirectTo: '/404'},

];

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled',
    useHash: true,
    initialNavigation: false,
});
