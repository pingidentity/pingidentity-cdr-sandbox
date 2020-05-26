import { Component, OnInit } from '@angular/core';
import { BundleAdminService, ProductAdminService } from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import { CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { Validators } from '@angular/forms';
import { map, switchMap, toArray } from 'rxjs/operators';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { from } from 'rxjs';

@Component({
  selector: 'app-bundle-add-product',
  templateUrl: './bundle-add-product.component.html',
})
export class BundleAddProductComponent implements OnInit {

    brandId: string;
    bundleId: string;

    productForm = new CdrFormGroup({
        product: new CdrFormSelect(null, 'Select product', [Validators.required], []),
    });

    constructor(
        private bundleApi: BundleAdminService,
        private productsApi: ProductAdminService,
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
    ) { }

    ngOnInit() {
        this.getConfigProp('brandId', true);
        this.getConfigProp('bundleId', true);

        this.productsApi.listProducts(this.brandId)
            .pipe(
                switchMap((products) => from(products)),
                map(({ id, name }) => ({ value: id, label: name })),
                toArray()
            )
            .subscribe((productsOptions) => {
                const productControl = this.productForm.get('product') as CdrFormSelect;
                productControl.options = productsOptions;

                if (productsOptions && productsOptions[0]) {
                    productControl.setValue(productsOptions[0].value);
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
        this.productForm.setSubmitted(true);

        if (this.productForm.invalid) {
            return;
        }

        this.bundleApi.addProductToProductBundle(this.brandId, this.bundleId, this.productForm.get('product').value)
            .subscribe(() => this.ref.close(true));
    }

}
