import { Component, OnInit } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { AbstractControl, Validators } from '@angular/forms';
import { BrandAdminService, DioBrand } from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormInput } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
  selector: 'app-brand-create-edit',
  templateUrl: './brand-create-edit.component.html',
  styleUrls: ['./brand-create-edit.component.scss']
})
export class BrandCreateEditComponent implements OnInit {

    brandForm: CdrFormGroup = new CdrFormGroup({
        id: new CdrFormInput('', 'ID'),
        name: new CdrFormInput('', 'Name', [Validators.required]),
        displayName: new CdrFormInput('', 'Display name', [Validators.required]),
    });

    brand: DioBrand;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private brandApi: BrandAdminService,
    ) { }

    ngOnInit() {
        const idControl = this.brandForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        if (this.config.data && this.config.data.brand) {
            this.brand = this.config.data.brand;

            this.brandForm.controls.id.setValue(this.brand.id);
            this.brandForm.controls.name.setValue(this.brand.name);
            this.brandForm.controls.displayName.setValue(this.brand.displayName);
        }
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.brandForm.setSubmitted(true);

        if (!this.brandForm.valid) {
            return;
        }

        const saving$ = this.brand
            ? this.brandApi.updateBrand(this.brand.id, this.brandForm.getRawValue())
            : this.brandApi.createBrand(this.brandForm.getRawValue());

        saving$.subscribe(
            (brand) => this.ref.close(brand),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.name': this.brandForm.get('name'),
                'cdrBanking.displayName': this.brandForm.get('displayName'),
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
