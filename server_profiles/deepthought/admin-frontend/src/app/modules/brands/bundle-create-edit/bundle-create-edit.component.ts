import { Component, OnInit } from '@angular/core';
import { BundleAdminService, DioProductBundle } from '@bizaoss/deepthought-admin-angular-client';
import { AbstractControl, Validators } from '@angular/forms';
import { CdrFormInput, CdrFormTextarea } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
    selector: 'app-bundle-create-edit',
    templateUrl: './bundle-create-edit.component.html',
    styleUrls: ['./bundle-create-edit.component.scss']
})
export class BundleCreateEditComponent implements OnInit {

    brandId: string;

    bundleForm = new CdrFormGroup({
        id:                 new CdrFormInput('', 'ID', [Validators.required]),
        name:               new CdrFormInput('', 'Name', [Validators.required]),
        description:        new CdrFormTextarea('', 'Description', [Validators.required]),
        additionalInfo:     new CdrFormInput('', 'Additional info'),
        additionalInfoUri:  new CdrFormInput('', 'Additional info URI'),
    });

    bundle: DioProductBundle;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private bundlesApi: BundleAdminService
    ) { }

    ngOnInit() {
        const idControl = this.bundleForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        this.getConfigProp('brandId', true);
        this.getConfigProp('bundle');

        if (this.bundle) {
            this.fillForm(this.bundle);
        } else {
            this.bundleForm.removeControl('id');
        }
    }

    getConfigProp(propName, required = false) {
        if (this.config.data && this.config.data[propName]) {
            this[propName] = this.config.data[propName];
        } else if (required) {
            this.ref.close(null);
            throw new Error(`'${propName}' is required param`);
        }
    }

    fillForm(bundle: DioProductBundle) {
        const {
            id = '',
            name = '',
            description = '',
            additionalInfo = '',
            additionalInfoUri = '',
        } = bundle;

        this.bundleForm.controls.id.setValue(id);
        this.bundleForm.controls.name.setValue(name);
        this.bundleForm.controls.description.setValue(description);
        this.bundleForm.controls.additionalInfo.setValue(additionalInfo);
        this.bundleForm.controls.additionalInfoUri.setValue(additionalInfoUri);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.bundleForm.setSubmitted(true);

        if (this.bundleForm.invalid) {
            return;
        }

        const saving$ = this.bundle
            ? this.bundlesApi.updateProductBundle(this.brandId, this.bundle.id, this.bundleForm.getRawValue())
            : this.bundlesApi.createProductBundle(this.brandId, this.bundleForm.getRawValue())
        ;

        saving$.subscribe(
            (bundle) => this.ref.close(bundle),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                id: this.bundleForm.get('id'),
                name: this.bundleForm.get('name'),
                description: this.bundleForm.get('description'),
                additionalInfo: this.bundleForm.get('additionalInfo'),
                additionalInfoUri: this.bundleForm.get('additionalInfoUri'),
            };

            for (const error of errors) {
                for (const field of error.fields) {
                    if (mapErrorFieldControl[field]) {
                        mapErrorFieldControl[field].setErrors({ SERVER: error.message });
                    }
                }
            }
        }
    }

}
