import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { map, switchMap } from 'rxjs/operators';
import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { ConfirmationService, DialogService } from 'primeng/api';
import { BrandAdminService, DioBrand } from '@bizaoss/deepthought-admin-angular-client';
import { BrandCreateEditComponent } from '../brand-create-edit/brand-create-edit.component';

@Component({
    selector: 'app-brand-view',
    templateUrl: './brand-view.component.html',
    styleUrls: ['./brand-view.component.scss'],
})
export class BrandViewComponent implements OnInit {

    brand: DioBrand;

    sidebarMenu: Array<{ routerLink: string; label: string; }> = [
        { routerLink: 'products',       label: 'Products' },
        { routerLink: 'bundles',        label: 'Bundles' },
        { routerLink: 'hr',             label: '' },
        { routerLink: 'editBrand',      label: 'Edit brand' },
        { routerLink: 'removeBrand',    label: 'Remove brand' },
    ];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private breadcrumbService: BreadcrumbService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private brandsApi: BrandAdminService,
    ) {}

    ngOnInit() {
        this.route.paramMap.pipe(
            switchMap((params: ParamMap) => this.fetchBrand(params.get('brandId')))
        ).subscribe();
    }

    fetchBrand(brandId) {
        return this.brandsApi.getBrand(brandId).pipe(
            map(this.onBrandFetchSuccess.bind(this))
        );
    }

    onBrandFetchSuccess(response) {
        this.brand = response;

        this.breadcrumbService.setItems([
            {
                label: 'Brands',
                routerLink: ['/brands']
            },
            {
                label: this.brand.name,
                routerLink: ['/brands', this.brand.id]
            }
        ]);
    }

    editBrand() {
        const ref = this.dialogService.open(BrandCreateEditComponent, {
            header: 'Create brand',
            width: '40%',
            data: { brand: this.brand }
        });

        ref.onClose
            .subscribe((editedBrand) => editedBrand ? this.fetchBrand(editedBrand.id).subscribe() : void(0));
    }

    removeBrand() {
        this.confirmationService.confirm({
            message: `Are you sure want to remove ${this.brand.name}?`,
            header: 'Remove brand',
            icon: null,
            accept: () => {
                this.brandsApi.deleteBrand(this.brand.id).subscribe(() => {
                    this.router.navigate(['/brands']);
                });
            },
            reject: () => {}
        });
    }
}
