import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { BundleAdminService, DioProductBundle, ProductAdminService } from '@bizaoss/deepthought-admin-angular-client';
import { ConfirmationService, DialogService } from 'primeng/api';
import { LayoutService } from '@app/layout/layout.service';
import { map, switchMap } from 'rxjs/operators';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { BundleCreateEditComponent } from '../../bundle-create-edit/bundle-create-edit.component';

@Component({
    selector: 'app-bundles-list',
    templateUrl: './brand-view-bundles.component.html',
    styleUrls: ['./brand-view-bundles.component.scss']
})
export class BrandViewBundlesComponent implements OnInit {

    public brandId: string;
    public bundles: Observable<DioProductBundle[]>;

    constructor(
        private route: ActivatedRoute,
        private layoutService: LayoutService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private productsApi: ProductAdminService,
        private bundlesApi: BundleAdminService,
    ) {}

    ngOnInit() {
        this.layoutService.togglePageLoader.emit(true);

        this.route.parent.paramMap.pipe(
            map((params: ParamMap) => this.brandId = params.get('brandId')),
            switchMap(() => this.fetchBundles()),
        ).subscribe(() => this.layoutService.togglePageLoader.emit(false));
    }

    fetchBundles() {
        return this.bundlesApi.listProductBundles(this.brandId).pipe(
            map(this.onFetchBundlesSuccess.bind(this))
        );
    }

    onFetchBundlesSuccess(bundles) {
        this.bundles = bundles;
    }

    createBundle() {
        const ref = this.dialogService.open(BundleCreateEditComponent, {
            header: 'Create bundle',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: {  brandId: this.brandId }
        });

        ref.onClose.subscribe((bundle) => bundle ? this.fetchBundles().subscribe() : void(0));
    }

    editBundle(bundle: DioProductBundle) {
        const ref = this.dialogService.open(BundleCreateEditComponent, {
            header: 'Edit bundle',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: {  brandId: this.brandId, bundle }
        });

        ref.onClose.subscribe((_bundle) => _bundle ? this.fetchBundles().subscribe() : void(0));
    }

    removeBundle(bundle: DioProductBundle) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove bunle "${bundle.name}"?`,
            header: 'Remove bundle',
            icon: null,
            accept: () => {
                this.bundlesApi
                    .deleteProductBundle(this.brandId, bundle.id)
                    .pipe(switchMap(() => this.fetchBundles()))
                    .subscribe();
            },
            reject: () => {}
        });
    }

}
