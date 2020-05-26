import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {TypeManagementService} from '@app/core/services/type-management.service';
import {TypeUtilityService} from '@app/core/services/type-utility.service';
import {ConfirmationService, DialogService} from 'primeng/api';
import {ProductFeeCreateEditComponent} from './product-fee-create-edit/product-fee-create-edit.component';
import {
    BankingProductFeeType,
    DioProductFee,
    FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import {map, switchMap} from 'rxjs/operators';

@Component({
    selector: 'app-product-view-fees',
    templateUrl: './product-view-fees.component.html',
    styleUrls: ['./product-view-fees.component.scss']
})
export class ProductViewFeesComponent implements OnInit {

    brandId: string;
    productId: string;

    fees: DioProductFee[] = [];

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
        private typeManager: TypeManagementService,
        private typeUtility: TypeUtilityService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private productsApi: ProductAdminService
    ) {
    }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');

            this.fetchFees().subscribe();
        });
    }

    fetchFees() {
        return this.productsApi.listProductFees(this.brandId, this.productId).pipe(
            map((fees) => {
                this.fees = fees
                this.fees.sort((a, b) => a.cdrBanking.name.localeCompare(b.cdrBanking.name))
            }),
        );
    }

    getFeeDetail(fee: DioProductFee, fieldName: string) {
        switch (fieldName) {
            case 'NAME':
                return fee.cdrBanking.name;
            case 'LABEL':
                return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTFEETYPE, fee.cdrBanking.feeType.toString());
            case 'INFO':
                return fee.cdrBanking.additionalInfo;
            case 'DETAIL':
                const feeDescription = [];

                if (fee.cdrBanking.amount != null) {
                    feeDescription.push(
                        fee.cdrBanking.feeType === BankingProductFeeType.PERIODIC
                            ? this.typeUtility.convertValueString(fee.cdrBanking.amount) + ' per ' + (fee.cdrBanking.additionalValue ? this.typeUtility.convertDuration(fee.cdrBanking.additionalValue) : '')
                            : this.typeUtility.convertValueString(fee.cdrBanking.amount)
                    );
                }

                if (fee.cdrBanking.balanceRate != null) {
                    feeDescription.push(
                        this.typeUtility.convertRateString(fee.cdrBanking.balanceRate) + ' charged on the account balance per ' + (fee.cdrBanking.accrualFrequency ? this.typeUtility.convertDuration(fee.cdrBanking.accrualFrequency) : '')
                    );
                }

                if (fee.cdrBanking.transactionRate != null) {
                    feeDescription.push(
                        this.typeUtility.convertRateString(fee.cdrBanking.transactionRate) + ' charged per transaction'
                    );
                }

                if (fee.cdrBanking.accruedRate != null) {
                    feeDescription.push(
                        this.typeUtility.convertRateString(fee.cdrBanking.accruedRate) + ' charged on interest charged per ' + (fee.cdrBanking.accrualFrequency ? this.typeUtility.convertDuration(fee.cdrBanking.accrualFrequency) : '')
                    );
                }

                return feeDescription.join(' + ');
        }
    }
    getDiscountType(type) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDISCOUNTTYPE, type);
    }

    getDiscountEligibilityType(type) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDISCOUNTELIGIBILITYTYPE, type);
    }


    createFee() {
        const ref = this.dialogService.open(ProductFeeCreateEditComponent, {
            header: 'Create fee',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: {brandId: this.brandId, productId: this.productId}
        });

        ref.onClose.subscribe((newFee) => {
            if (!newFee) {
                return;
            }

            this.fetchFees().subscribe();
        });
    }

    editFee(fee: DioProductFee) {
        const ref = this.dialogService.open(ProductFeeCreateEditComponent, {
            header: 'Edit fee',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: {brandId: this.brandId, productId: this.productId, fee}
        });

        ref.onClose.subscribe((editedFee) => {
            if (!editedFee) {
                return;
            }

            this.fetchFees().subscribe();
        });
    }

    removeFee(fee: DioProductFee) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove fee?`,
            header: 'Remove fee',
            icon: null,
            accept: () => {
                this.productsApi
                    .deleteProductFee(this.brandId, this.productId, fee.id)
                    .pipe(switchMap(() => this.fetchFees()))
                    .subscribe();
            },
            reject: () => {
            }
        });
    }

    feesEmpty() {
        return this.fees == null || this.fees.length == 0;
    }
}

