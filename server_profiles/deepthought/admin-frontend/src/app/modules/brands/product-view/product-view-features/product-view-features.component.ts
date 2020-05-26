import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import {
    BankingProductFeatureType,
    DioProductFeature,
    FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { from } from 'rxjs';
import { reduce, switchMap } from 'rxjs/operators';
import { ConfirmationService, DialogService } from 'primeng/api';
import { ProductFeatureCreateEditComponent } from './product-feature-create-edit/product-feature-create-edit.component';
import { TypeUtilityService } from '@app/core/services/type-utility.service';
import { TypeManagementService } from '@app/core/services/type-management.service';

@Component({
    selector: 'app-product-view-features',
    templateUrl: './product-view-features.component.html',
    styleUrls: ['./product-view-features.component.scss']
})
export class ProductViewFeaturesComponent implements OnInit {

    brandId: string;
    productId: string;

    features$: any;

    activeTabIndex = -1;
    private hasFeatures = false;

    constructor(
        private route: ActivatedRoute,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private typeUtility: TypeUtilityService,
        private productsApi: ProductAdminService,
        private typeManager: TypeManagementService
    ) { }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');

            this.fetchFeatures();
        });
    }

    onTabOpen(e: { index: number }) {
        this.activeTabIndex = e.index;
    }

    fetchFeatures() {
        this.features$ = this.productsApi.listProductFeatures(this.brandId, this.productId).pipe(
            switchMap((features: DioProductFeature[]) => from(features)),
            reduce((acc: { [key: string]: DioProductFeature[] }, feature: DioProductFeature) => {
                if (!acc[feature.cdrBanking.featureType]) {
                    acc[feature.cdrBanking.featureType] = [];
                }

                acc[feature.cdrBanking.featureType].push(feature);
                this.hasFeatures = true;
                return acc;
            }, {})
        );
    }

    getFeatureValue(feature: DioProductFeature) {
        switch (feature.cdrBanking.featureType) {
            case BankingProductFeatureType.CARDACCESS:
                return feature.cdrBanking.additionalValue;
            case BankingProductFeatureType.ADDITIONALCARDS:
                if (feature.cdrBanking.additionalValue != null) {
                    return 'Up to ' + feature.cdrBanking.additionalValue + ' additional cards';
                }
                return 'Unlimited additional cards';
            case BankingProductFeatureType.FREETXNS:
                return 'Up to ' + feature.cdrBanking.additionalValue + ' free transactions per month';
            case BankingProductFeatureType.FREETXNSALLOWANCE:
                return 'Up to a total of ' + this.typeUtility.convertValueString(feature.cdrBanking.additionalValue) + ' transaction fees included';
            case BankingProductFeatureType.LOYALTYPROGRAM:
                return feature.cdrBanking.additionalValue + ' membership available';
            case BankingProductFeatureType.INTERESTFREE:
                return 'Up to ' + this.typeUtility.convertDuration(feature.cdrBanking.additionalValue) + ' interest free on purchases';
            case BankingProductFeatureType.INTERESTFREETRANSFERS:
                return 'Up to ' + this.typeUtility.convertDuration(feature.cdrBanking.additionalValue) + ' interest free on balance transfers';
            case BankingProductFeatureType.DIGITALBANKING:
                return 'Includes access to digital banking services';
            case BankingProductFeatureType.DIGITALWALLET:
                return 'Includes access to ' + feature.cdrBanking.additionalValue;
            case BankingProductFeatureType.BILLPAYMENT:
                if (feature.cdrBanking.additionalValue != null) {
                    return 'Access to ' + feature.cdrBanking.additionalValue;
                }
                break;
            case BankingProductFeatureType.COMPLEMENTARYPRODUCTDISCOUNTS:
                return feature.cdrBanking.additionalValue;
            case BankingProductFeatureType.BONUSREWARDS:
                return 'Up to ' + feature.cdrBanking.additionalValue + ' bonus loyalty points available';
            case BankingProductFeatureType.NOTIFICATIONS:
                return feature.cdrBanking.additionalValue;
        }

        return null;
    }

    getFeatureCategoryName(category) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTFEATURETYPE, category);
    }

    createFeature() {
        const ref = this.dialogService.open(ProductFeatureCreateEditComponent, {
            header: 'Create feature',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId }
        });

        ref.onClose.subscribe((newFeature) => {
            if (!newFeature) {
                return;
            }

            this.fetchFeatures();
        });
    }

    editFeature(feature: DioProductFeature) {
        const ref = this.dialogService.open(ProductFeatureCreateEditComponent, {
            header: 'Edit feature',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId, feature }
        });

        ref.onClose.subscribe((editedFeature) => {
            if (!editedFeature) {
                return;
            }
            this.fetchFeatures();
        });
    }

    removeFeature(feature: DioProductFeature) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove feature?`,
            header: 'Remove feature',
            icon: null,
            accept: () => {
                this.productsApi.deleteProductFeature(this.brandId, this.productId, feature.id).subscribe(() => {
                    this.fetchFeatures();
                });
            },
            reject: () => {}
        });
    }

    featuresEmpty() {
        return this.hasFeatures != true;
    }
}
