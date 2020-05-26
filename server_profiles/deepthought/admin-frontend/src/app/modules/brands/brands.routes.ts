import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { CatalogueProductViewComponent } from './product-view/catalogue-product-view.component';
import { ProductViewDetailsComponent } from './product-view/product-view-details/product-view-details.component';
import { ProductViewFeaturesComponent } from './product-view/product-view-features/product-view-features.component';
import { BrandsListComponent } from './brands-list/brands-list.component';
import { BrandViewComponent } from './brand-view/brand-view.component';
import { ProductViewConstraintsComponent } from './product-view/product-view-constraints/product-view-constraints.component';
import { ProductViewFeesComponent } from './product-view/product-view-fees/product-view-fees.component';
import { ProductViewRatesLendingComponent } from './product-view/rates/product-view-rates-lending/product-view-rates-lending.component';
import { ProductViewRatesDepositComponent } from './product-view/rates/product-view-rates-deposit/product-view-rates-deposit.component';
import { BrandViewBundlesComponent } from './brand-view/brand-view-bundles/brand-view-bundles.component';
import { BrandViewProductsComponent } from './brand-view/brand-view-products/brand-view-products.component';
import { BundleViewComponent } from './bundle-view/bundle-view.component';
import { ProductViewCardArtComponent } from './product-view/product-view-card-art/product-view-card-art.component';
import { ProductFeeCreateEditComponent } from './product-view/product-view-fees/product-fee-create-edit/product-fee-create-edit.component';

const routes: Routes = [

    {
        path: '',
        component: BrandsListComponent
    },
    {
        path: ':brandId',
        component: BrandViewComponent,
        children: [
            { path: '', redirectTo: 'products' },
            { path: 'products', component: BrandViewProductsComponent },
            { path: 'bundles', component: BrandViewBundlesComponent },
        ]
    },
    {
        path: ':brandId/products/:productId',
        component: CatalogueProductViewComponent,
        children: [
            { path: '',                 component: ProductViewDetailsComponent },
            { path: 'features',         component: ProductViewFeaturesComponent },
            { path: 'constraints',      component: ProductViewConstraintsComponent },
            { path: 'fees',             component: ProductViewFeesComponent },
            { path: 'lending-rates',    component: ProductViewRatesLendingComponent },
            { path: 'deposit-rates',    component: ProductViewRatesDepositComponent },
            { path: 'card-arts',        component: ProductViewCardArtComponent },
        ]
    },
    {
        path: ':brandId/bundles/:bundleId',
        component: BundleViewComponent,
    },
    {
        path: ':brandId/products/:productId/fees/create',
        component: ProductFeeCreateEditComponent
    },
    {
        path: ':brandId/products/:productId/fees/:feeId/edit',
        component: ProductFeeCreateEditComponent
    }
];

export const BrandsRoutes: ModuleWithProviders = RouterModule.forChild(routes);
