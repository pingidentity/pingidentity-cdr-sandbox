import { Component, OnInit } from '@angular/core';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import {
    BankingProductDiscountEligibilityType,
    BankingProductDiscountType,
    BankingProductDiscountV1, BankingProductFeeDiscountEligibilityV1,
    FormFieldType,
} from '@bizaoss/deepthought-admin-angular-client';
import { DynamicDialogConfig, DynamicDialogRef, SelectItem } from 'primeng/api';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { FormArray } from '@angular/forms';

@Component({
  selector: 'app-product-fee-discount-create-edit',
  templateUrl: './product-fee-discount-create-edit.component.html',
  styleUrls: ['./product-fee-discount-create-edit.component.scss']
})
export class ProductFeeDiscountCreateEditComponent implements OnInit {

    discountForm = new CdrFormGroup({
        discountType: new CdrFormSelect('', 'Discount type'),
        description: new CdrFormInput('', 'Description'),
        amount: new CdrFormInput('', 'Amount'),
        balanceRate: new CdrFormInput('', 'Balance rate'),
        transactionRate: new CdrFormInput('', 'Transaction rate'),
        accruedRate: new CdrFormInput('', 'Accrued rate'),
        feeRate: new CdrFormInput('', 'Fee rate'),
        additionalValue: new CdrFormInput('', 'Additional value'),
        additionalInfo: new CdrFormInput('', 'Additional info'),
        additionalInfoUri: new CdrFormInput('', 'Additional info URI'),
    });

    discountEligibilityTypeOptions: SelectItem[] = [];

    eligibilitysForm = new FormArray([]);

    discount: BankingProductDiscountV1;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private typeManager: TypeManagementService
    ) { }

    ngOnInit() {

        this.discountEligibilityTypeOptions = Object.keys(BankingProductDiscountEligibilityType).map((key) => ({
            value: BankingProductDiscountEligibilityType[key],
            label: this.typeManager.getLabel(
                FormFieldType.BANKINGPRODUCTDISCOUNTELIGIBILITYTYPE,
                BankingProductDiscountEligibilityType[key]
            ),
        }));

        const discountTypeOptions = Object.keys(BankingProductDiscountType).map((key) => ({
            value: BankingProductDiscountType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDISCOUNTTYPE, BankingProductDiscountType[key]),
        }));

        const discountTypeControl = this.discountForm.get('discountType') as CdrFormSelect;
        discountTypeControl.options = discountTypeOptions;
        discountTypeControl.setValue(discountTypeOptions[0].value);

        if (this.config.data && this.config.data.discount) {
            this.discount = this.config.data.discount;
        }

        this.fillForm(this.discount);
    }

    fillForm(discount: BankingProductDiscountV1 = {} as BankingProductDiscountV1) {
        const fields: Array<{ form: CdrFormGroup, fieldName: string, defaultValue?: any }> = [
            {
                form: this.discountForm,
                fieldName: 'discountType',
                defaultValue: (this.discountForm.get('discountType') as CdrFormSelect).options[0].value
            },
            { form: this.discountForm, fieldName: 'description' },
            { form: this.discountForm, fieldName: 'amount' },
            { form: this.discountForm, fieldName: 'balanceRate' },
            { form: this.discountForm, fieldName: 'transactionRate' },
            { form: this.discountForm, fieldName: 'accruedRate' },
            { form: this.discountForm, fieldName: 'feeRate' },
            { form: this.discountForm, fieldName: 'additionalValue' },
            { form: this.discountForm, fieldName: 'additionalInfo' },
            { form: this.discountForm, fieldName: 'additionalInfoUri' },
        ];

        for (const field of fields) {
            field.form.get(field.fieldName).setValue(discount[field.fieldName] || field.defaultValue || null);
        }

        if (discount.eligibility && discount.eligibility.length) {
            for (const eligibility of discount.eligibility) {
                this.addEligibility(eligibility);
            }
        }
    }

    addEligibility(eligibility: BankingProductFeeDiscountEligibilityV1 = {} as BankingProductFeeDiscountEligibilityV1) {
        const {
            discountEligibilityType = this.discountEligibilityTypeOptions[0].value,
            additionalValue = '',
            additionalInfo = '',
            additionalInfoUri = '',
        } = eligibility;

        this.eligibilitysForm.push(new CdrFormGroup({
            discountEligibilityType: new CdrFormSelect(discountEligibilityType, 'Type', [], this.discountEligibilityTypeOptions),
            additionalValue: new CdrFormInput(additionalValue, 'Additional value'),
            additionalInfo: new CdrFormInput(additionalInfo, 'Additional info'),
            additionalInfoUri: new CdrFormInput(additionalInfoUri, 'Additional info URI'),
        }));
    }
    removeEligibility(index: number) {
        this.eligibilitysForm.removeAt(index);
    }


    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.discountForm.setSubmitted(true);

        if (!this.discountForm.valid) {
            return;
        }

        const data = this.discountForm.value;
        data.additionalValue = data.additionalValue || null;

        data.eligibility = this.eligibilitysForm.value;
        if(data.eligibility != null) {
            data.eligibility.additionalValue = data.eligibility.additionalValue || null;
        }

        this.ref.close(data);
    }

}
