import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { LayoutService } from '@app/layout/layout.service';
import { map, switchMap } from 'rxjs/operators';
import { ConfirmationService, DialogService } from 'primeng/api';
import { ProductCreateEditComponent } from '../product-create-edit/product-create-edit.component';
import { BrandAdminService, DioBrand, DioProduct, ProductAdminService } from '@bizaoss/deepthought-admin-angular-client';

@Component({
    selector: 'app-catalogue-product-view',
    templateUrl: './catalogue-product-view.component.html',
    styleUrls: ['./catalogue-product-view.component.scss']
})
export class CatalogueProductViewComponent implements OnInit {

    brand: DioBrand;
    product: DioProduct;

    outletComponent: any;

    sidebarMenu: Array<{ label: string; routerLink: string; }> = [
        { routerLink: '',               label: 'Basic details' },
        { routerLink: 'features',       label: 'Features' },
        { routerLink: 'constraints',    label: 'Eligibility & Constraints' },
        { routerLink: 'fees',           label: 'Fees' },
        { routerLink: 'lending-rates',  label: 'Lending rates' },
        { routerLink: 'deposit-rates',  label: 'Deposit rates' },
        { routerLink: 'card-arts',      label: 'Card arts' },
        { routerLink: 'hr',             label: '' },
        { routerLink: 'edit',           label: 'Edit product' },
        { routerLink: 'remove',         label: 'Remove product' },
    ];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private breadcrumbService: BreadcrumbService,
        private layoutService: LayoutService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private brandsApi: BrandAdminService,
        private productsApi: ProductAdminService
    ) {}

    ngOnInit() {

        this.layoutService.togglePageLoader.emit(true);

        this.route.paramMap.subscribe((params: ParamMap) => {
            const brandId = params.get('brandId');
            const productId = params.get('productId');

            this.brandsApi.getBrand(brandId)
                .pipe(
                    map((brand) => this.brand = brand),
                    switchMap(() => this.fetchProduct(productId))
                )
                .subscribe();
        });
    }

    fetchProduct(productId) {
        return this.productsApi.getProduct(this.brand.id, productId).pipe(
            map(this.onProductFetchSuccess.bind(this))
        );
    }

    onProductFetchSuccess(product) {
        this.product = product;
        this.sendProductToOutletComponent();

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
                label: 'Products',
                routerLink: ['/brands', this.brand.id, 'products']
            },
            {
                label: this.product.name,
                routerLink: ['/brands', this.brand.id, 'products', this.product.id]
            }
        ]);

        this.layoutService.togglePageLoader.emit(false);
    }

    onOutletActivate(componentReference: any) {
        this.outletComponent = componentReference;
        this.sendProductToOutletComponent();
    }

    sendProductToOutletComponent() {
        if (this.outletComponent && this.outletComponent.setProduct) {
            this.outletComponent.setProduct(this.product);
        }
    }

    onEditProduct() {
        const ref = this.dialogService.open(ProductCreateEditComponent, {
            header: 'Edit product',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: {
                brandId: this.brand.id,
                product: this.product
            }
        });

        ref.onClose.subscribe((product) => {
            if (!product) {
                return;
            }

            this.fetchProduct(this.product.id).subscribe();
        });
    }

    onRemoveProduct() {
        this.confirmationService.confirm({
            message: `Are you sure want to remove ${this.product.name}?`,
            header: 'Remove product',
            icon: null,
            accept: () => {
                this.productsApi.deleteProduct(this.brand.id, this.product.id).subscribe(() => {
                    this.router.navigate(['/brands', this.brand.id]);
                });
            },
            reject: () => {}
        });
    }
}
