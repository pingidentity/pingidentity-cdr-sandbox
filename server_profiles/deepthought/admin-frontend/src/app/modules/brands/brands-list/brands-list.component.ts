import { Component, OnInit } from '@angular/core';
import { DialogService } from 'primeng/api';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { LayoutService } from '@app/layout/layout.service';

import { BrandAdminService, DioBrand } from '@bizaoss/deepthought-admin-angular-client';
import { BrandCreateEditComponent } from '../brand-create-edit/brand-create-edit.component';

@Component({
    selector: 'app-products-list',
    templateUrl: './brands-list.component.html',
})
export class BrandsListComponent implements OnInit {

    public brands$: Observable<DioBrand[]>;

    constructor(
        private breadcrumbService: BreadcrumbService,
        private layoutService: LayoutService,
        private brandsApi: BrandAdminService,
        private dialogService: DialogService
    ) {}

    ngOnInit() {
        this.layoutService.togglePageLoader.emit(true);

        this.breadcrumbService.setItems([{
            label: 'Brands',
            routerLink: '/brands'
        }]);

        this.fetchBrands();
    }

    fetchBrands() {
        this.brands$ = this.brandsApi.listBrands().pipe(
            finalize(() => this.layoutService.togglePageLoader.emit(false))
        );
    }

    createBrand() {
        const ref = this.dialogService.open(BrandCreateEditComponent, {
            header: 'Create brand',
            width: '40%',
        });

        ref.onClose.subscribe(() => this.fetchBrands());
    }

}
