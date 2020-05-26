import { Component, OnInit } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { AbstractControl, Validators } from '@angular/forms';
import {
    BankingProductConstraintType,
    DioProductConstraint,
    DioSchemeType,
    FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';

@Component({
  selector: 'app-product-constraint-create-edit',
  templateUrl: './product-constraint-create-edit.component.html',
  styleUrls: ['./product-constraint-create-edit.component.scss']
})
export class ProductConstraintCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;

    constraintForm = new CdrFormGroup({
        id:             new CdrFormInput('', '', [Validators.required]),
        schemeType:     new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        constraintType:     new CdrFormSelect('', 'Constraint type', [Validators.required]),
        additionalValue:    new CdrFormInput('', 'Additional value'),
        additionalInfo:     new CdrFormInput('', 'Additional info'),
        additionalInfoUri:  new CdrFormInput('', 'Additional info URI'),
    });

    constraint: DioProductConstraint;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) { }

    ngOnInit() {
        this.constraintForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.constraintForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        const schemeTypeOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const schemeTypeOptionsControl = this.constraintForm.controls.schemeType as CdrFormSelect;
        schemeTypeOptionsControl.options = schemeTypeOptions;
        schemeTypeOptionsControl.setValue(schemeTypeOptions[0].value);
        schemeTypeOptionsControl.disable();
        schemeTypeOptionsControl.isVisible = false;

        const constraintTypeOptions = Object.keys(BankingProductConstraintType).map((key) => ({
            value: BankingProductConstraintType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTCONSTRAINTTYPE, BankingProductConstraintType[key]),
        }));

        const constraintTypeControl = this.cdrBankingForm.controls.constraintType as CdrFormSelect;
        constraintTypeControl.options = constraintTypeOptions;
        constraintTypeControl.setValue(constraintTypeOptions[0].value);

        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);
        this.getConfigProp('constraint');

        if (this.constraint) {
            this.fillForm(this.constraint);
        } else {
            this.constraintForm.removeControl('id');
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

    fillForm(constraint: DioProductConstraint) {
        this.constraintForm.controls.id.setValue(constraint.id);
        this.constraintForm.controls.schemeType.setValue(constraint.schemeType);

        this.cdrBankingForm.controls.constraintType.setValue(constraint.cdrBanking.constraintType);
        this.cdrBankingForm.controls.additionalValue.setValue(constraint.cdrBanking.additionalValue);
        this.cdrBankingForm.controls.additionalInfo.setValue(constraint.cdrBanking.additionalInfo);
        this.cdrBankingForm.controls.additionalInfoUri.setValue(constraint.cdrBanking.additionalInfoUri);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.constraintForm.setSubmitted(true);

        if (!this.constraintForm.valid) {
            return;
        }

        const saving$ = this.constraint
            ? this.productsApi.updateProductConstraint(this.brandId, this.productId, this.constraint.id, this.constraintForm.getRawValue())
            : this.productsApi.createProductConstraint(this.brandId, this.productId, this.constraintForm.getRawValue())
        ;

        saving$.subscribe(
            (constraint) => this.ref.close(constraint),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.additionalValue':     this.cdrBankingForm.get('additionalValue'),
                'cdrBanking.additionalInfo':      this.cdrBankingForm.get('additionalInfo'),
                'cdrBanking.additionalInfoUri':   this.cdrBankingForm.get('additionalInfoUri'),
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
