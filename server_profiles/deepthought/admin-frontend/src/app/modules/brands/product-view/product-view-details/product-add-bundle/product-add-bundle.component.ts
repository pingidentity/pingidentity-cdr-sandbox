import { Component, OnInit } from '@angular/core';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import { CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { Validators } from '@angular/forms';
import { BundleAdminService, ProductAdminService } from '@bizaoss/deepthought-admin-angular-client';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { map, switchMap, toArray } from 'rxjs/operators';
import { from } from 'rxjs';

@Component({
    selector: 'app-product-add-bundle',
    templateUrl: './product-add-bundle.component.html',
})
export class ProductAddBundleComponent implements OnInit {

    brandId: string;
    productId: string;

    bundleForm = new CdrFormGroup({
        bundle: new CdrFormSelect(null, 'Select bundle', [Validators.required], []),
    });

    constructor(
        private bundleApi: BundleAdminService,
        private productsApi: ProductAdminService,
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
    ) { }

    ngOnInit() {
        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);

        this.bundleApi.listProductBundles(this.brandId)
            .pipe(
                switchMap((bundles) => from(bundles)),
                map(({ id, name }) => ({ value: id, label: name })),
                toArray()
            )
            .subscribe((bundlesOptions) => {
                const bundleControl = this.bundleForm.get('bundle') as CdrFormSelect;
                bundleControl.options = bundlesOptions;

                if (bundlesOptions && bundlesOptions[0]) {
                    bundleControl.setValue(bundlesOptions[0].value);
                }
            });
    }

    getConfigProp(propName, required = false) {
        if (this.config.data && this.config.data[propName]) {
            this[propName] = this.config.data[propName];
        } else if (required) {
            this.ref.close(null);
            throw new Error(`'${propName}' is required param`);
        }
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.bundleForm.setSubmitted(true);

        if (this.bundleForm.invalid) {
            return;
        }

        this.bundleApi
            .addProductToProductBundle(this.brandId, this.bundleForm.get('bundle').value, this.productId)
            .subscribe(() => this.ref.close(true))
    }

}
