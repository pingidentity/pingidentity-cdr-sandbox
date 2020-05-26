import { Component, OnInit } from '@angular/core';
import { AbstractControl, Validators } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import {
    BankingProductEligibilityType,
    DioProductEligibility,
    DioSchemeType, FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
  selector: 'app-product-eligibility-create-edit',
  templateUrl: './product-eligibility-create-edit.component.html',
  styleUrls: ['./product-eligibility-create-edit.component.scss']
})
export class ProductEligibilityCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;

    eligibilityForm = new CdrFormGroup({
        id:         new CdrFormInput('', '', [Validators.required]),
        schemeType: new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        eligibilityType:    new CdrFormSelect('', 'Eligibility type', [Validators.required]),
        additionalValue:    new CdrFormInput('', 'Additional value'),
        additionalInfo:     new CdrFormInput('', 'Additional info'),
        additionalInfoUri:  new CdrFormInput('', 'Additional info URI'),
    });

    eligibility: DioProductEligibility;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) { }

    ngOnInit() {
        this.eligibilityForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.eligibilityForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        const typeOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const typeControl = this.eligibilityForm.controls.schemeType as CdrFormSelect;
        typeControl.options = typeOptions;
        typeControl.setValue(typeOptions[0].value);
        typeControl.disable();
        typeControl.isVisible = false;

        const eligibilityTypeOptions = Object.keys(BankingProductEligibilityType).map((key) => ({
            value: BankingProductEligibilityType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTELIGIBILITYTYPE, BankingProductEligibilityType[key]),
        }));

        const eligibilityTypeControl = this.cdrBankingForm.controls.eligibilityType as CdrFormSelect;
        eligibilityTypeControl.options = eligibilityTypeOptions;
        eligibilityTypeControl.setValue(eligibilityTypeOptions[0].value);

        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);
        this.getConfigProp('eligibility');

        if (this.eligibility) {
            this.fillForm(this.eligibility);
        } else {
            this.eligibilityForm.removeControl('id');
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

    fillForm(eligibility: DioProductEligibility) {
        this.eligibilityForm.controls.id.setValue(eligibility.id);
        this.eligibilityForm.controls.schemeType.setValue(eligibility.schemeType);

        this.cdrBankingForm.controls.eligibilityType.setValue(eligibility.cdrBanking.eligibilityType);
        this.cdrBankingForm.controls.additionalValue.setValue(eligibility.cdrBanking.additionalValue);
        this.cdrBankingForm.controls.additionalInfo.setValue(eligibility.cdrBanking.additionalInfo);
        this.cdrBankingForm.controls.additionalInfoUri.setValue(eligibility.cdrBanking.additionalInfoUri);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.eligibilityForm.setSubmitted(true);

        if (!this.eligibilityForm.valid) {
            return;
        }

        const saving$ = this.eligibility
            ? this.productsApi.updateProductEligibility(this.brandId, this.productId, this.eligibility.id, this.eligibilityForm.getRawValue())
            : this.productsApi.createProductEligibility(this.brandId, this.productId, this.eligibilityForm.getRawValue())
        ;

        saving$.subscribe(
            (eligibility) => this.ref.close(eligibility),
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
