import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, Validators } from '@angular/forms';
import { TypeManagementService } from '@app/core/services/type-management.service';
import {
    BankingProductDiscountType,
    BankingProductFeeV1,
    BankingProductDiscountV1,
    BankingProductFeeType,
    DioProductFee,
    DioSchemeType, FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { finalize, map, mergeMap, switchMap } from 'rxjs/operators';
import { iif, of, Subject } from 'rxjs';
import { LayoutService } from '@app/layout/layout.service';
import { BreadcrumbService } from '@app/layout/breadcrumb.service';
import { ProductEligibilityCreateEditComponent } from '../../product-view-constraints/product-eligibility-create-edit/product-eligibility-create-edit.component';
import { ProductFeeDiscountCreateEditComponent } from '../product-fee-discount-create-edit/product-fee-discount-create-edit.component';
import { ConfirmationService, DialogService } from 'primeng/api';

@Component({
  selector: 'app-product-fee-create-edit',
  templateUrl: './product-fee-create-edit.component.html',
  styleUrls: ['./product-fee-create-edit.component.scss']
})
export class ProductFeeCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;
    feeId: string;

    feeForm = new CdrFormGroup({
        id:     new CdrFormInput(null, '', [Validators.required]),
        schemeType:   new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        feeType:            new CdrFormSelect(null, 'Type', [Validators.required]),
        name:               new CdrFormInput('', 'Name', []),
        amount:             new CdrFormInput('', 'Amount', []),
        balanceRate:        new CdrFormInput('', 'Balance rate', []),
        transactionRate:    new CdrFormInput('', 'Transaction rate', []),
        accruedRate:        new CdrFormInput('', 'Accrued rate', []),
        accrualFrequency:   new CdrFormInput('', 'Accrual frequency', []),
        currency:           new CdrFormInput('', 'Currency'),
        additionalValue:    new CdrFormInput(null, 'Additional value'),
        additionalInfo:     new CdrFormInput('', 'Additional info'),
        additionalInfoUri:  new CdrFormInput('', 'Additional info URI'),
    });

    fee: DioProductFee;
    discounts: BankingProductDiscountV1[] = [];
    discountsErrors: { [key: string]: string } = {};

    discountDetailsOptions: Array<{ key: string; label: string; }> = [
        { key: 'discountType', label: 'Discount type' },
        { key: 'description', label: 'Description' },
        { key: 'amount', label: 'Amount' },
        { key: 'balanceRate', label: 'Balance rate' },
        { key: 'transactionRate', label: 'Transaction rate' },
        { key: 'accruedRate', label: 'Accrued rate' },
        { key: 'feeRate', label: 'Fee rate' },
        { key: 'additionalValue', label: 'Additional value' },
        { key: 'additionalInfo', label: 'Additional info' },
        { key: 'additionalInfoUri', label: 'Additional info URI' },
    ];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private typeManager: TypeManagementService,
        private productsApi: ProductAdminService,
        private layout: LayoutService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
    ) { }

    ngOnInit() {
        const params = this.route.snapshot.paramMap;

        this.brandId = params.get('brandId');
        this.productId = params.get('productId');
        this.feeId = params.get('feeId');

        if (this.feeId) {
            this.layout.togglePageLoader.emit(true);

            this.fetchFee().pipe(
                map((fee) => this.init(fee)),
                finalize(() => this.layout.togglePageLoader.emit(false))
            ).subscribe();
        } else {
            this.init(null);
        }
    }

    init(fee) {
        this.fee = fee;

        this.feeForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.feeForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        const schemeTypeControlOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const schemeTypeControl = this.feeForm.controls.schemeType as CdrFormSelect;
        schemeTypeControl.options = schemeTypeControlOptions;
        schemeTypeControl.setValue(schemeTypeControlOptions[0].value);
        schemeTypeControl.disable();
        schemeTypeControl.isVisible = false;

        const feeTypeOptions = Object.keys(BankingProductFeeType).map((key) => ({
            value: BankingProductFeeType[key],
            label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTFEETYPE, BankingProductFeeType[key]),
        }));

        const feeTypeControl = this.cdrBankingForm.controls.feeType as CdrFormSelect;
        feeTypeControl.options = feeTypeOptions;
        feeTypeControl.setValue(feeTypeOptions[0].value);

        if (this.fee) {
            this.fillForm(this.fee);
        } else {
            this.feeForm.removeControl('id');
        }
    }

    fetchFee() {
        if (!this.feeId) {
            return void 0;
        }
        return this.productsApi.getProductFee(this.brandId, this.productId, this.feeId);
    }

    fillForm(fee: DioProductFee) {
        const { id, schemeType, cdrBanking = {} as BankingProductFeeV1 } = fee;

        this.feeForm.get('id').setValue(id);
        this.feeForm.get('schemeType').setValue(schemeType);

        const {
            feeType,
            name,
            amount,
            balanceRate,
            transactionRate,
            accruedRate,
            accrualFrequency,
            currency,
            additionalValue,
            additionalInfo,
            additionalInfoUri,
            discounts = []
        } = cdrBanking;

        this.cdrBankingForm.get('feeType').setValue(feeType);
        this.cdrBankingForm.get('name').setValue(name);
        this.cdrBankingForm.get('amount').setValue(amount);
        this.cdrBankingForm.get('balanceRate').setValue(balanceRate);
        this.cdrBankingForm.get('transactionRate').setValue(transactionRate);
        this.cdrBankingForm.get('accruedRate').setValue(accruedRate);
        this.cdrBankingForm.get('accrualFrequency').setValue(accrualFrequency);
        this.cdrBankingForm.get('currency').setValue(currency);
        this.cdrBankingForm.get('additionalValue').setValue(additionalValue);
        this.cdrBankingForm.get('additionalInfo').setValue(additionalInfo);
        this.cdrBankingForm.get('additionalInfoUri').setValue(additionalInfoUri);

        this.discounts = discounts;
    }

    createEditDiscount(discount?: BankingProductDiscountV1) {
        const isNew = !discount;

        const ref = this.dialogService.open(ProductFeeDiscountCreateEditComponent, {
            header: isNew ? 'Add discount' : 'Edit discount',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { discount: discount || null }
        });

        ref.onClose.subscribe((_discount) => {
            if (!_discount) return;

            if (isNew) {
                this.discounts = [...this.discounts, _discount];
            } else {
                this.discounts = this.discounts.map((_) => _ === discount ? _discount : _);
            }
        });
    }
    removeDiscount(discount: BankingProductDiscountV1) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove this discount?`,
            header: 'Remove discount',
            icon: null,
            accept: () => {
                this.discounts = this.discounts.filter((_) => _ !== discount);
            },
            reject: () => {}
        });
    }

    getDiscountType(type) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDISCOUNTTYPE, type);
    }
    getDiscountEligibilityType(type) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDISCOUNTELIGIBILITYTYPE, type);
    }

    redirectToFeesPage() {
        this.router.navigate(['/brands', this.brandId, 'products', this.productId, 'fees']);
    }

    onCancel() {
        if (!this.feeForm.touched && !this.discounts.length) {
            return this.redirectToFeesPage();
        }

        this.confirmationService.confirm({
            message: `Are you sure want leave this page? Changes will be lost!`,
            header: 'Confirm cancel',
            icon: null,
            accept: () => {
                return this.redirectToFeesPage();
            },
            reject: () => {}
        });
    }

    onSave() {
        this.discountsErrors = {};
        this.feeForm.setSubmitted(true);

        if (!this.feeForm.valid) {
            return;
        }

        const data = this.feeForm.getRawValue();

        data.cdrBanking.discounts = this.discounts;

        const saving$ = this.fee
            ? this.productsApi.updateProductFee(this.brandId, this.productId, this.fee.id, data)
            : this.productsApi.createProductFee(this.brandId, this.productId, data)
        ;

        saving$.subscribe(
            _ => this.redirectToFeesPage(),
            this.onSavingError.bind(this)
        );

    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.name':              this.cdrBankingForm.get('name'),
                'cdrBanking.amount':            this.cdrBankingForm.get('amount'),
                'cdrBanking.balanceRate':       this.cdrBankingForm.get('balanceRate'),
                'cdrBanking.transactionRate':   this.cdrBankingForm.get('transactionRate'),
                'cdrBanking.accruedRate':       this.cdrBankingForm.get('accruedRate'),
                'cdrBanking.accrualFrequency':  this.cdrBankingForm.get('accrualFrequency'),
                'cdrBanking.currency':          this.cdrBankingForm.get('currency'),
                'cdrBanking.value':             this.cdrBankingForm.get('additionalValue'),
                'cdrBanking.additionalValue':   this.cdrBankingForm.get('additionalValue'),
                'cdrBanking.additionalInfo':    this.cdrBankingForm.get('additionalInfo'),
                'cdrBanking.additionalInfoUri': this.cdrBankingForm.get('additionalInfoUri'),
            };

            for (const error of errors) {
                for (const field of error.fields) {
                    if (mapErrorFieldControl[field]) {
                        mapErrorFieldControl[field].setErrors({ SERVER: error.message });
                    }
                }
            }

            for (let i = 0; i < this.discounts.length; i++) {
                for (const error of errors) {
                    for (const field of error.fields) {
                        if (field.startsWith(`cdrBanking.discounts[${i}]`)) {
                            this.discountsErrors[i] = error.message;
                        }
                    }
                }
            }

        }
    }

}
