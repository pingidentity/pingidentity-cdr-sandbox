import { Pipe, PipeTransform } from '@angular/core';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { BankingProductFeatureType, DioProductFeature } from '@bizaoss/deepthought-admin-angular-client';
import { TypeUtilityService } from '@app/core/services/type-utility.service';

@Pipe({
    name: 'convertFeatureCriteria'
})
export class ConvertFeatureCriteriaPipe implements PipeTransform {

    constructor(
        private typeManager: TypeManagementService,
        private typeUtility: TypeUtilityService
    ) {}

    // TODO: Move to helpers
    getFeatureCriteria(feature: DioProductFeature, fieldName: string) {
        if (fieldName === 'LABEL') {
            return this.typeManager.getLabel('BANKING_PRODUCT_FEATURE_TYPE', feature.cdrBanking.featureType.toString());
        }

        if (fieldName === 'VALUE') {
            switch (feature.cdrBanking.featureType) {
                case BankingProductFeatureType.CARDACCESS: return feature.cdrBanking.additionalValue;
                case BankingProductFeatureType.ADDITIONALCARDS:
                    return !!feature.cdrBanking.additionalValue
                        ? `Up to ${feature.cdrBanking.additionalValue} additional cards`
                        : 'Unlimited additional cards';
                case BankingProductFeatureType.FREETXNS: return `Up to ${feature.cdrBanking.additionalValue} free transactions per month`;
                case BankingProductFeatureType.FREETXNSALLOWANCE: return `Up to a total of ${this.typeUtility.convertValueString(feature.cdrBanking.additionalValue)} transaction fees included`;
                case BankingProductFeatureType.LOYALTYPROGRAM: return `${feature.cdrBanking.additionalValue} membership available`;
                case BankingProductFeatureType.INTERESTFREE: return `Up to ${this.typeUtility.convertDuration(feature.cdrBanking.additionalValue)} interest free on purchases`;
                case BankingProductFeatureType.INTERESTFREETRANSFERS: return `Up to ${this.typeUtility.convertDuration(feature.cdrBanking.additionalValue)} interest free on balance transfers`;
                case BankingProductFeatureType.DIGITALBANKING: return 'Includes access to digital banking services';
                case BankingProductFeatureType.DIGITALWALLET: return `Includes access to ${feature.cdrBanking.additionalValue}`;
                case BankingProductFeatureType.BILLPAYMENT:
                    return !!feature.cdrBanking.additionalValue
                        ? `Access to ${feature.cdrBanking.additionalValue}`
                        : null;
                case BankingProductFeatureType.COMPLEMENTARYPRODUCTDISCOUNTS: return feature.cdrBanking.additionalValue;
                case BankingProductFeatureType.BONUSREWARDS: return `Up to ${feature.cdrBanking.additionalValue} bonus loyalty points available`;
                case BankingProductFeatureType.NOTIFICATIONS: return feature.cdrBanking.additionalValue;
            }
        }

        return null;
    }

    transform(value: any, ...args: any[]): any {
        return this.getFeatureCriteria(value, args[0]);
    }

}
