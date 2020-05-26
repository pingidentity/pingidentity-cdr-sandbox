import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { TypeUtilityService } from '@app/core/services/type-utility.service';
import {
    BankingProductDepositRateType, BankingProductLendingRateType, BankingProductRateTierApplicationMethod,
    CommonUnitOfMeasureType,
    DioProductRateDeposit, DioProductRateLending, FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { ConfirmationService, DialogService } from 'primeng/api';
import { ProductRateDepositCreateEditComponent } from './product-rate-deposit-create-edit/product-rate-deposit-create-edit.component';
import {falseIfMissing} from 'protractor/built/util';

@Component({
  selector: 'app-product-view-rates-deposit',
  templateUrl: './product-view-rates-deposit.component.html',
  styleUrls: ['./../product-view-rates.component.scss']
})
export class ProductViewRatesDepositComponent implements OnInit {

    brandId: string;
    productId: string;


    rateData: {
        [key in keyof typeof BankingProductDepositRateType]: DioProductRateDeposit[];
    };

    constructor(
        private route: ActivatedRoute,
        private typeManager: TypeManagementService,
        private typeUtility: TypeUtilityService,
        private productsApi: ProductAdminService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        public typeUtilityService: TypeUtilityService,
    ) { }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');
            this.rateData = {
                'FIXED': [],
                'VARIABLE': [],
                'INTRODUCTORY': [],
                'FLOATING': [],
                'MARKETLINKED': [],
                'BONUS': [],
                'BUNDLEBONUS': [],
            };
            this.fetchRates();
        });
    }

    fetchRates() {
        this.productsApi.listProductRateDeposits(this.brandId, this.productId).subscribe((rates) => {
            this.rateData = {
                'FIXED': [],
                'VARIABLE': [],
                'INTRODUCTORY': [],
                'FLOATING': [],
                'MARKETLINKED': [],
                'BONUS': [],
                'BUNDLEBONUS': [],
            };
            rates.forEach((rate) => {
                if(!this.rateData[rate.cdrBanking.depositRateType]) {
                    this.rateData[rate.cdrBanking.depositRateType] = [];
                }
                this.rateData[rate.cdrBanking.depositRateType].push(rate);
            });
        });
    }

    getDepositDetail(depositRate: DioProductRateDeposit, fieldName: string) {
        switch (fieldName) {
            case 'NAME':
                return this.typeManager.getLabel('BANKING_PRODUCT_DEPOSIT_RATE_TYPE', depositRate.cdrBanking.depositRateType);
            case 'DETAIL':
                return `${this.typeUtility.convertRateString(depositRate.cdrBanking.rate)} per annum.`;
            case 'INFO':
                const detail = [];

                if (depositRate.cdrBanking.calculationFrequency != null) {
                    detail.push(`Calculated every ${this.typeUtility.convertDuration(depositRate.cdrBanking.calculationFrequency)} from account balance.`);
                }

                if (depositRate.cdrBanking.applicationFrequency != null) {
                    detail.push(`Applied to account every ${this.typeUtility.convertDuration(depositRate.cdrBanking.applicationFrequency)}.`);
                }

                if (depositRate.cdrBanking.additionalInfo) {
                    detail.push(depositRate.cdrBanking.additionalInfo);
                }

                if (depositRate.cdrBanking.depositRateType === BankingProductDepositRateType.BONUS
                    || depositRate.cdrBanking.depositRateType === BankingProductDepositRateType.FLOATING
                    || depositRate.cdrBanking.depositRateType === BankingProductDepositRateType.MARKETLINKED
                ) {
                    detail.push(depositRate.cdrBanking.additionalValue);
                }

                if (depositRate.cdrBanking.depositRateType === BankingProductDepositRateType.BUNDLEBONUS) {
                    detail.push(`Available with the ${depositRate.cdrBanking.additionalValue} bundle`);
                }

                if (depositRate.cdrBanking.depositRateType === BankingProductDepositRateType.INTRODUCTORY) {
                    detail.push(`Introductory rate applies for ${this.typeUtility.convertDuration(depositRate.cdrBanking.additionalValue)}.`);
                }

                return detail.join(' ');
            default: return '';
        }
    }

    createRate() {
        const ref = this.dialogService.open(ProductRateDepositCreateEditComponent, {
            header: 'Create rate',
            width: '70%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId }
        });

        ref.onClose.subscribe((newRate) => newRate ? this.fetchRates() : void(0));
    }

    editRate(depositRate: DioProductRateDeposit) {
        const ref = this.dialogService.open(ProductRateDepositCreateEditComponent, {
            header: 'Edit rate',
            width: '70%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId, rate: depositRate }
        });

        ref.onClose.subscribe((editedRate) => editedRate ? this.fetchRates() : void(0));
    }

    removeRate(depositRate: DioProductRateDeposit) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove rate?`,
            header: 'Remove rate',
            icon: null,
            accept: () => {
                this.productsApi
                    .deleteProductRateDeposit(this.brandId, this.productId, depositRate.id)
                    .subscribe(() => this.fetchRates());
            },
            reject: () => {}
        });
    }

    getTierUnitOfMeasureText(value) {
        return this.typeManager.getLabel(FormFieldType.COMMONUNITOFMEASURETYPE, value);
    }

    getTierApplicationMethod(value) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTRATETIERAPPLICATIONMETHOD, value);
    }

    getTierUnit(value) {
        return this.typeManager.getLabel(FormFieldType.COMMONUNITOFMEASURETYPE, value);
    }

    getRateType(value) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTDEPOSITRATETYPE, value);
    }


    getFrequency(value) {
        if (!value) {
            return 'N/A';
        }
        return this.typeUtilityService.convertDuration(value);
    }

    getRateDescription(rate: DioProductRateDeposit) {
      if(rate.cdrBanking.depositRateType == BankingProductDepositRateType.FIXED || rate.cdrBanking.depositRateType == BankingProductDepositRateType.INTRODUCTORY) {
          return 'for ' + this.getFrequency(rate.cdrBanking.additionalValue);
      } else {
          return '';
      }
    }

    hasAdditionalValue(rate: DioProductRateDeposit) {
        if(rate.cdrBanking.depositRateType != BankingProductDepositRateType.FIXED && rate.cdrBanking.depositRateType != BankingProductDepositRateType.INTRODUCTORY) {
            if(rate.cdrBanking.additionalValue != null && rate.cdrBanking.additionalValue != '') {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    ratesEmpty() {
        if(this.rateData == null) { return true }
        let nonEmpty = 0;
        for(let keyName of Object.keys(this.rateData)) {
            if(this.rateData[keyName] != null && Object.keys(this.rateData[keyName]).length != 0) {
                nonEmpty++;
            }
        }
        return nonEmpty == 0;
    }
}
