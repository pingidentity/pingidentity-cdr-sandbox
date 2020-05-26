import { Component, OnInit } from '@angular/core';
import { CdrFormGroup } from '@app/shared/forms/crd-form-group.class';
import { CdrFormInput, CdrFormSelect } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';
import { AbstractControl, Validators } from '@angular/forms';
import {
    DioProductCardArt,
    DioSchemeType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';

@Component({
  selector: 'app-product-card-art-create-edit',
  templateUrl: './product-card-art-create-edit.component.html',
  styleUrls: ['./product-card-art-create-edit.component.scss']
})
export class ProductCardArtCreateEditComponent implements OnInit {

    brandId: string;
    productId: string;

    cardArtForm = new CdrFormGroup({
        id:         new CdrFormInput('', '', [Validators.required]),
        schemeType: new CdrFormSelect(null, 'Scheme type', [Validators.required], []),
    });

    cdrBankingForm = new CdrFormGroup({
        title:    new CdrFormInput('', 'Title'),
        imageUri: new CdrFormInput('', 'Image URI'),
    });

    cardArt: DioProductCardArt;

    constructor(
        private ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private productsApi: ProductAdminService,
    ) { }

    ngOnInit() {
        this.cardArtForm.addControl('cdrBanking', this.cdrBankingForm);

        const idControl = this.cardArtForm.controls.id as CdrFormInput;
        idControl.isVisible = false;

        const typeOptions = Object.keys(DioSchemeType).map((key) => ({
            value: DioSchemeType[key],
            label: DioSchemeType[key],
        }));

        const typeControl = this.cardArtForm.controls.schemeType as CdrFormSelect;
        typeControl.options = typeOptions;
        typeControl.setValue(typeOptions[0].value);
        typeControl.disable();
        typeControl.isVisible = false;

        this.getConfigProp('brandId', true);
        this.getConfigProp('productId', true);
        this.getConfigProp('cardArt');

        if (this.cardArt) {
            this.fillForm(this.cardArt);
        } else {
            this.cardArtForm.removeControl('id');
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

    fillForm(cardArt: DioProductCardArt) {
        this.cardArtForm.controls.id.setValue(cardArt.id);
        this.cardArtForm.controls.schemeType.setValue(cardArt.schemeType);

        this.cdrBankingForm.controls.title.setValue(cardArt.cdrBanking.title);
        this.cdrBankingForm.controls.imageUri.setValue(cardArt.cdrBanking.imageUri);
    }

    onCancel() {
        this.ref.close(null);
    }

    onSave() {
        this.cardArtForm.setSubmitted(true);

        if (!this.cardArtForm.valid) {
            return;
        }

        const saving$ = this.cardArt
            ? this.productsApi.updateProductCardArt(this.brandId, this.productId, this.cardArt.id, this.cardArtForm.getRawValue())
            : this.productsApi.createProductCardArt(this.brandId, this.productId, this.cardArtForm.getRawValue())
        ;

        saving$.subscribe(
            (cardArt) => this.ref.close(cardArt),
            this.onSavingError.bind(this)
        );
    }

    onSavingError({ error: { type, validationErrors: errors } }) {
        if (type === 'VALIDATION_ERROR') {

            const mapErrorFieldControl: { [key: string]: AbstractControl } = {
                'cdrBanking.title': this.cdrBankingForm.get('title'),
                'cdrBanking.imageUri': this.cdrBankingForm.get('imageUri'),
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
