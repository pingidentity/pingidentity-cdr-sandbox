import { Component, OnInit } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef, SelectItem } from 'primeng/api';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import {
    BankingProductCategory,
    DioProduct,
    DioSchemeType,
    FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import * as  moment from 'moment';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import {
    CdrFormCheckbox,
    CdrFormDate,
    CdrFormInput,
    CdrFormSelect,
    CdrFormTextarea
} from '@app/shared/forms/cdr-form-control/cdr-form-control.component';

@Component({
  selector: 'app-product-create-edit',
  templateUrl: './product-create-edit.component.html',
  styleUrls: ['./product-create-edit.component.scss']
})
export class ProductCreateEditComponent implements OnInit {

    types: SelectItem[];

    productForm: CdrFormGroup = new CdrFormGroup({
        id:             new CdrFormInput('ad372c1e-8076-440d-a66c-db86a6507947', 'ID'),
        name:           new CdrFormInput('', 'Name', [Validators.required]),
        description:    new CdrFormTextarea('', 'Description', [Validators.required]),
        schemeType:     new CdrFormSelect(null, 'Scheme type', [Validators.required]),
    });

    cdrBankingForm: CdrFormGroup = new CdrFormGroup({
        effectiveFrom:      new CdrFormDate(null, 'Effective from'),
        effectiveTo:        new CdrFormDate(null, 'Effective to'),
        productCategory:    new CdrFormSelect('', 'Product category', [Validators.required]),
        applicationUri:     new CdrFormInput('', 'Application URI'),
        tailored:           new CdrFormCheckbox(false, 'Tailored'),
    });

    additionalInformationForm: CdrFormGroup = new CdrFormGroup({
        overviewUri:        new CdrFormInput('', 'Overview URI'),
        termsUri:           new CdrFormInput('', 'Terms URI'),
        eligibilityUri:     new CdrFormInput('', 'Eligibility URI'),
        feesAndPricingUri:  new CdrFormInput('', 'Fees and Pricing URI'),
        bundleUri:          new CdrFormInput('', 'Bundle URI'),
    });

    brandId: string;

    product: DioProduct;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private fb: FormBuilder,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) { }

    ngOnInit() {
        this.productForm.addControl('cdrBanking', this.cdrBankingForm);
        this.cdrBankingForm.addControl('additionalInformation', this.additionalInformationForm);

        const idControl = this.productForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        // Init scheme type options ********************************************************************************************************

        const schemeTypeOptions = Object.keys(DioSchemeType)
            .map((key) => ({ value: DioSchemeType[key], label: DioSchemeType[key] }));

        const schemeTypeControl = this.productForm.controls.schemeType as CdrFormSelect;
        schemeTypeControl.isVisible = false;
        schemeTypeControl.options = schemeTypeOptions;
        schemeTypeControl.setValue(schemeTypeOptions[0].value);

        // Init product category options ***************************************************************************************************

        const productCategoryOptions = Object.keys(BankingProductCategory)
            .map((key) => ({
                value: BankingProductCategory[key],
                label: this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTCATEGORY, BankingProductCategory[key])
            }));

        (this.cdrBankingForm.controls.productCategory as CdrFormSelect).options = productCategoryOptions;
        this.cdrBankingForm.controls.productCategory.setValue(productCategoryOptions[0].value);

        // Check brandId *******************************************************************************************************************

        if (this.config.data && this.config.data.brandId) {
            this.brandId = this.config.data.brandId;
        } else {
            this.ref.close(null);
            throw new Error('brandId is required param');
        }

        // Fill form data ******************************************************************************************************************

        if (this.config.data && this.config.data.product) {
            this.fillForm(this.config.data.product);
        }
    }

    fillForm(product: DioProduct) {
        this.product = product;

        this.productForm.controls.id.setValue(this.product.id);
        this.productForm.controls.name.setValue(this.product.name);
        this.productForm.controls.schemeType.setValue(this.product.schemeType);
        this.productForm.controls.description.setValue(this.product.description);

        if (product.cdrBanking) {
            const effectiveFrom = product.cdrBanking.effectiveFrom ? moment(product.cdrBanking.effectiveFrom).toDate() : null;
            const effectiveTo = product.cdrBanking.effectiveTo ? moment(product.cdrBanking.effectiveTo).toDate() : null;

            this.cdrBankingForm.controls.effectiveFrom.setValue(effectiveFrom);
            this.cdrBankingForm.controls.effectiveTo.setValue(effectiveTo);
            this.cdrBankingForm.controls.productCategory.setValue(product.cdrBanking.productCategory);
            this.cdrBankingForm.controls.applicationUri.setValue(product.cdrBanking.applicationUri);
            this.cdrBankingForm.controls.tailored.setValue(product.cdrBanking.tailored);

            if (product.cdrBanking.additionalInformation) {
                this.additionalInformationForm.controls.overviewUri.setValue(product.cdrBanking.additionalInformation.overviewUri);
                this.additionalInformationForm.controls.termsUri.setValue(product.cdrBanking.additionalInformation.termsUri);
                this.additionalInformationForm.controls.eligibilityUri.setValue(product.cdrBanking.additionalInformation.eligibilityUri);
                this.additionalInformationForm.controls.feesAndPricingUri.setValue(product.cdrBanking.additionalInformation.feesAndPricingUri);
                this.additionalInformationForm.controls.bundleUri.setValue(product.cdrBanking.additionalInformation.bundleUri);
            }
        }
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.productForm.setSubmitted(true);

        if (!this.productForm.valid) {
            return;
        }

        const saving$ = this.product
            ? this.productsApi.updateProduct(this.brandId, this.product.id, this.productForm.value)
            : this.productsApi.createProduct(this.brandId, this.productForm.value);

        saving$.subscribe(
            (product) => this.ref.close(product),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                id: this.productForm.get('id'),
                name: this.productForm.get('name'),
                description: this.productForm.get('description'),
                schemeType: this.productForm.get('schemeType'),
                'cdrBanking.effectiveFrom': this.cdrBankingForm.get('effectiveFrom'),
                'cdrBanking.effectiveTo': this.cdrBankingForm.get('effectiveTo'),
                'cdrBanking.productCategory': this.cdrBankingForm.get('productCategory'),
                'cdrBanking.applicationUri': this.cdrBankingForm.get('applicationUri'),
                'cdrBanking.tailored': this.cdrBankingForm.get('tailored'),
                'cdrBanking.additionalInformation.overviewUri': this.additionalInformationForm.get('overviewUri'),
                'cdrBanking.additionalInformation.termsUri': this.additionalInformationForm.get('termsUri'),
                'cdrBanking.additionalInformation.eligibilityUri': this.additionalInformationForm.get('eligibilityUri'),
                'cdrBanking.additionalInformation.feesAndPricingUri': this.additionalInformationForm.get('feesAndPricingUri'),
                'cdrBanking.additionalInformation.bundleUri': this.additionalInformationForm.get('bundleUri'),
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
