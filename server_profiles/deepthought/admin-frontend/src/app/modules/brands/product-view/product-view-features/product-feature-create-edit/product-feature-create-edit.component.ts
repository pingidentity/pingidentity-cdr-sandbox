import { Component, OnInit } from '@angular/core';
import { AbstractControl, Validators } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import {
    BankingProductFeatureType,
    DioProductFeature,
    DioSchemeType, FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
    selector: 'app-product-feature-create-edit',
    templateUrl: './product-feature-create-edit.component.html',
    styleUrls: ['./product-feature-create-edit.component.scss']
})
export class ProductFeatureCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;

    featureForm = new CdrFormGroup({
        id:             new CdrFormInput('', '', [Validators.required]),
        schemeType:     new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        featureType:        new CdrFormSelect('', 'Feature type', [Validators.required]),
        additionalValue:    new CdrFormInput(null, 'Additional value'),
        additionalInfo:     new CdrFormInput('', 'Additional Info'),
        additionalInfoUri:  new CdrFormInput('', 'Additional Info URI'),
    });

    feature: DioProductFeature;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) {}

    ngOnInit() {
        this.featureForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.featureForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        const typeOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const typeControl = this.featureForm.controls.schemeType as CdrFormSelect;
        typeControl.options = typeOptions;
        typeControl.setValue(typeOptions[0].value);
        typeControl.disable();
        typeControl.isVisible = false;

        const featureTypeOptions = Object.keys(BankingProductFeatureType).map((key) => ({
            value: BankingProductFeatureType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTFEATURETYPE, BankingProductFeatureType[key]),
        }));

        const featureTypeControl = this.cdrBankingForm.controls.featureType as CdrFormSelect;
        featureTypeControl.options = featureTypeOptions;
        featureTypeControl.setValue(featureTypeOptions[0].value);

        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);
        this.getConfigProp('feature');

        if (this.feature) {
            this.fillForm(this.feature);
        } else {
            this.featureForm.removeControl('id');
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

    fillForm(feature: DioProductFeature) {
        this.featureForm.controls.id.setValue(feature.id);
        this.featureForm.controls.schemeType.setValue(feature.schemeType);

        this.cdrBankingForm.controls.featureType.setValue(feature.cdrBanking.featureType);
        this.cdrBankingForm.controls.additionalValue.setValue(feature.cdrBanking.additionalValue);
        this.cdrBankingForm.controls.additionalInfo.setValue(feature.cdrBanking.additionalInfo);
        this.cdrBankingForm.controls.additionalInfoUri.setValue(feature.cdrBanking.additionalInfoUri);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.featureForm.setSubmitted(true);

        if (!this.featureForm.valid) {
            return;
        }

        const data = this.featureForm.getRawValue();
        data.cdrBanking.additionalValue = data.cdrBanking.additionalValue || null;

        const saving$ = this.feature
            ? this.productsApi.updateProductFeature(this.brandId, this.productId, this.feature.id, data)
            : this.productsApi.createProductFeature(this.brandId, this.productId, data)
        ;

        saving$.subscribe(
            (feature) => this.ref.close(feature),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.additionalValue': this.cdrBankingForm.get('additionalValue'),
                'cdrBanking.additionalInfo': this.cdrBankingForm.get('additionalInfo'),
                'cdrBanking.additionalInfoUri': this.cdrBankingForm.get('additionalInfoUri'),
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
