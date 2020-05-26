import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, Validators } from '@angular/forms';
import {
    CdrFormDuration,
    CdrFormInput,
    CdrFormSelect
} from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import {
    BankingProductLendingRateV1,
    BankingProductLendingRateType,
    BankingProductRateTierApplicationMethod,
    BankingProductRateTierV1,
    CommonUnitOfMeasureType,
    DioProductRateLending,
    DioSchemeType, FormFieldType,
    ProductAdminService, BankingProductLendingRateInterestPaymentType
} from '@bizaoss/deepthought-admin-angular-client';
import { DynamicDialogConfig, DynamicDialogRef, SelectItem } from 'primeng/api';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
    selector: 'app-product-rate-lending-create-edit',
    templateUrl: './product-rate-lending-create-edit.component.html',
    styleUrls: ['./product-rate-lending-create-edit.component.scss']
})
export class ProductRateLendingCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;

    rateForm = new CdrFormGroup({
        id:             new CdrFormInput('', 'ID', [Validators.required]),
        schemeType:     new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        lendingRateType:        new CdrFormSelect('', 'Rate type', [Validators.required]),
        rate:                   new CdrFormInput(null, 'Rate'),
        comparisonRate:         new CdrFormInput(null, 'Comparison Rate'),
        applicationFrequency:   new CdrFormDuration(null, 'Application frequency'),
        calculationFrequency:   new CdrFormDuration(null, 'Calculation frequency'),
        interestPaymentDue:     new CdrFormSelect(null, 'Interest Payment Due'),
        additionalValue:        new CdrFormInput(null, 'Additional value'),
        additionalInfo:         new CdrFormInput(null, 'Additional info'),
        additionalInfoUri:      new CdrFormInput(null, 'Additional info URI'),
    });

    tiersForm = new FormArray([]);

    rate: DioProductRateLending;

    unitOfMeasureOptions: SelectItem[] = [];
    rateTierApplicationMethodOptions: SelectItem[] = [];

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) {}

    ngOnInit() {
        this.rateForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.rateForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        // *********************************************************************************************************************************

        const schemeTypeOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const schemeTypeOptionsControl = this.rateForm.controls.schemeType as CdrFormSelect;
        schemeTypeOptionsControl.options = schemeTypeOptions;
        schemeTypeOptionsControl.setValue(schemeTypeOptions[0].value);
        schemeTypeOptionsControl.disable();
        schemeTypeOptionsControl.isVisible = false;

        // *********************************************************************************************************************************

        const rateTypeOptions = Object.keys(BankingProductLendingRateType).map((key) => ({
            value: BankingProductLendingRateType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTLENDINGRATETYPE, BankingProductLendingRateType[key]),
        }));

        const rateTypeControl = this.cdrBankingForm.controls.lendingRateType as CdrFormSelect;
        rateTypeControl.options = rateTypeOptions;
        rateTypeControl.setValue(rateTypeOptions[0].value);

        // *********************************************************************************************************************************

        const interestPaymentDueOptions = Object.keys(BankingProductLendingRateInterestPaymentType).map((key) => ({
            value: BankingProductLendingRateInterestPaymentType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTLENDINGRATEINTERESTPAYMENTTYPE, BankingProductLendingRateInterestPaymentType[key]),
        }));

        const interestPaymentDueControl = this.cdrBankingForm.controls.interestPaymentDue as CdrFormSelect;
        interestPaymentDueControl.options = interestPaymentDueOptions;

        // *********************************************************************************************************************************

        this.unitOfMeasureOptions = Object.keys(CommonUnitOfMeasureType).map((key) => ({
            value: CommonUnitOfMeasureType[key],
            label: this.typeManager.getLabel(FormFieldType.COMMONUNITOFMEASURETYPE, CommonUnitOfMeasureType[key]),
        }));

        this.rateTierApplicationMethodOptions = Object.keys(BankingProductRateTierApplicationMethod).map((key) => ({
            value: BankingProductRateTierApplicationMethod[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTRATETIERAPPLICATIONMETHOD, BankingProductRateTierApplicationMethod[key]),
        }));

        // *********************************************************************************************************************************

        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);
        this.getConfigProp('rate');

        if (this.rate) {
            this.fillForm(this.rate);
        } else {
            this.rateForm.removeControl('id');
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

    fillForm(lendingRate: DioProductRateLending) {
        const {
            id,
            schemeType,
            cdrBanking = {} as BankingProductLendingRateV1
        } = lendingRate;

        this.rateForm.controls.id.setValue(id);
        this.rateForm.controls.schemeType.setValue(schemeType);

        const {
            lendingRateType = null,
            rate = null,
            comparisonRate = null,
            calculationFrequency = null,
            applicationFrequency = null,
            interestPaymentDue = null,
            tiers = [],
            additionalValue = null,
            additionalInfo = null,
            additionalInfoUri = null,
        } = cdrBanking;

        this.cdrBankingForm.controls.lendingRateType.setValue(lendingRateType);
        this.cdrBankingForm.controls.rate.setValue(rate);
        this.cdrBankingForm.controls.comparisonRate.setValue(comparisonRate);
        this.cdrBankingForm.controls.calculationFrequency.setValue(calculationFrequency);
        this.cdrBankingForm.controls.applicationFrequency.setValue(applicationFrequency);
        this.cdrBankingForm.controls.interestPaymentDue.setValue(interestPaymentDue);
        this.cdrBankingForm.controls.additionalValue.setValue(additionalValue);
        this.cdrBankingForm.controls.additionalInfo.setValue(additionalInfo);
        this.cdrBankingForm.controls.additionalInfoUri.setValue(additionalInfoUri);

        if (tiers && tiers.length) {
            for (const tier of tiers) {
                this.addTier(tier);
            }
        }
    }

    addTier(tier: BankingProductRateTierV1 = {} as BankingProductRateTierV1) {
        const {
            name = null,
            unitOfMeasure = this.unitOfMeasureOptions[0].value,
            rateApplicationMethod = this.rateTierApplicationMethodOptions[0].value,
            minimumValue = 0,
            maximumValue = 0,
            applicabilityConditions: {
                additionalInfo = null,
                additionalInfoUri = null
            } = {}
        } = tier;

        this.tiersForm.push(new CdrFormGroup({
            name:                   new CdrFormInput(name, 'Name'),
            unitOfMeasure:          new CdrFormSelect(unitOfMeasure, 'Unit of measure', [], this.unitOfMeasureOptions),
            rateApplicationMethod:  new CdrFormSelect(rateApplicationMethod, 'Rate application method', [], this.rateTierApplicationMethodOptions),
            minimumValue:           new CdrFormInput(minimumValue, 'Minimum value', [], 'number'),
            maximumValue:           new CdrFormInput(maximumValue, 'Maximum value', [], 'number'),
            applicabilityConditions: new CdrFormGroup({
                additionalInfo: new CdrFormInput(additionalInfo, 'Additional info'),
                additionalInfoUri: new CdrFormInput(additionalInfoUri, 'Additional info URI')
            })
        }));
    }

    removeTier(index: number) {
        this.tiersForm.removeAt(index);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.rateForm.setSubmitted(true);

        if (!this.rateForm.valid) {
            return;
        }

        const rateData = this.rateForm.getRawValue();

        rateData.cdrBanking.tiers = this.tiersForm.value;
        rateData.cdrBanking.additionalValue = rateData.cdrBanking.additionalValue || null;

        const saving$ = this.rate
            ? this.productsApi.updateProductRateLending(this.brandId, this.productId, this.rate.id, rateData)
            : this.productsApi.createProductRateLending(this.brandId, this.productId, rateData)
        ;

        saving$.subscribe(
            (rate) => this.ref.close(rate),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.lendingRateType':       this.cdrBankingForm.get('lendingRateType'),
                'cdrBanking.rate':                  this.cdrBankingForm.get('rate'),
                'cdrBanking.calculationFrequency':  this.cdrBankingForm.get('calculationFrequency'),
                'cdrBanking.applicationFrequency':  this.cdrBankingForm.get('applicationFrequency'),
                'cdrBanking.interestPaymentDue':    this.cdrBankingForm.get('interestPaymentDue'),
                'cdrBanking.additionalValue':       this.cdrBankingForm.get('additionalValue'),
                'cdrBanking.additionalInfo':        this.cdrBankingForm.get('additionalInfo'),
                'cdrBanking.additionalInfoUri':     this.cdrBankingForm.get('additionalInfoUri'),
            };

            for (let i = 0; i < this.tiersForm.controls.length; i++) {
                mapErrorFieldControl[`cdrBanking.tiers[${i}].name`] = this.tiersForm.controls[i].get('name');
                mapErrorFieldControl[`cdrBanking.tiers[${i}].unitOfMeasure`] = this.tiersForm.controls[i].get('unitOfMeasure');
                mapErrorFieldControl[`cdrBanking.tiers[${i}].rateApplicationMethod`] = this.tiersForm.controls[i].get('rateApplicationMethod');
                mapErrorFieldControl[`cdrBanking.tiers[${i}].minimumValue`] = this.tiersForm.controls[i].get('minimumValue');
                mapErrorFieldControl[`cdrBanking.tiers[${i}].maximumValue`] = this.tiersForm.controls[i].get('maximumValue');
            }

            for (const error of errors) {
                for (const field of error.fields) {
                    if (mapErrorFieldControl[field]) {
                        mapErrorFieldControl[field].setErrors({ SERVER: error.message });
                    }
                }
            }
        }
    }

    trackByFn(index, item) {
        return index;
    }

}
