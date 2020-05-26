import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { ConfirmationService, DialogService } from 'primeng/api';
import { BrandAdminService, BundleAdminService, DioBrand, DioProductBundle } from '@bizaoss/deepthought-admin-angular-client';
import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { BundleAddProductComponent } from './bundle-add-product/bundle-add-product.component';
import { BundleCreateEditComponent } from '../bundle-create-edit/bundle-create-edit.component';
import { zip } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Component({
    selector: 'app-bundle-view',
    templateUrl: './bundle-view.component.html',
    styleUrls: ['./bundle-view.component.scss']
})
export class BundleViewComponent implements OnInit {

    brandId: string;
    bundleId: string;

    brand: DioBrand;
    bundle: DioProductBundle;
    products: DioProductBundle[];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private bundleApi: BundleAdminService,
        private brandsApi: BrandAdminService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private breadcrumbService: BreadcrumbService,
    ) { }

    ngOnInit() {
        this.route.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.bundleId = params.get('bundleId');

            this.brandsApi.getBrand(this.brandId)
                .pipe(
                    map((brand) => this.brand = brand),
                    switchMap(() => zip(
                        this.fetchBundle(),
                        this.fetchProducts()
                    ))
                )
                .subscribe();
        });
    }

    fetchBundle() {
        return this.bundleApi.getProductBundle(this.brandId, this.bundleId).pipe(
            map((bundle) => {
                this.bundle = bundle;

                this.breadcrumbService.setItems([
                    {
                        label: 'Brands',
                        routerLink: ['/brands']
                    },
                    {
                        label: this.brand.name,
                        routerLink: ['/brands', this.brand.id]
                    },
                    {
                        label: 'Bundles',
                        routerLink: ['/brands', this.brand.id, 'bundles']
                    },
                    {
                        label: bundle.name,
                        routerLink: ['/brands', this.brand.id, 'bundles', bundle.id]
                    }
                ]);

                return bundle;
            })
        );
    }

    fetchProducts() {
        return this.bundleApi.listProductsForBundle(this.brandId, this.bundleId).pipe(
            map((products) => this.products = products)
        );
    }

    editBundle() {
        const ref = this.dialogService.open(BundleCreateEditComponent, {
            header: 'Edit bundle',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, bundle: this.bundle }
        });

        ref.onClose.subscribe((_bundle) => _bundle ? this.fetchBundle().subscribe() : void(0));
    }

    removeBundle() {
        this.confirmationService.confirm({
            message: `Are you sure want to remove bundle "${this.bundle.name}"?`,
            header: 'Remove bundle',
            icon: null,
            accept: () => {
                this.bundleApi
                    .deleteProductBundle(this.brandId, this.bundle.id)
                    .subscribe(() => this.router.navigate(['/brands', this.brandId]));
            },
            reject: () => {}
        });
    }

    addProductToBundle() {
        const ref = this.dialogService.open(BundleAddProductComponent, {
            header: 'Add product to bundle',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, bundleId: this.bundleId }
        });

        ref.onClose.subscribe((isAdded) => isAdded ? this.fetchProducts().subscribe() : void(0));
    }

    removeProductFromBundle(product: DioProductBundle) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove product "${product.name}" from this bundle?`,
            header: 'Remove product',
            icon: null,
            accept: () => {
                this.bundleApi
                    .deleteProductFromProductBundle(this.brandId, this.bundleId, product.id)
                    .subscribe(() => this.fetchProducts().subscribe());
            },
            reject: () => {}
        });
    }

}
