import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ConfirmationService, DialogService } from 'primeng/api';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { TypeUtilityService } from '@app/core/services/type-utility.service';
import { ProductEligibilityCreateEditComponent } from './product-eligibility-create-edit/product-eligibility-create-edit.component';
import { ProductConstraintCreateEditComponent } from './product-constraint-create-edit/product-constraint-create-edit.component';
import {
    BankingProductConstraintType,
    BankingProductEligibilityType,
    DioProductConstraint,
    DioProductEligibility, FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-product-view-constraints',
  templateUrl: './product-view-constraints.component.html',
  styleUrls: ['./product-view-constraints.component.scss']
})
export class ProductViewConstraintsComponent implements OnInit {

    brandId: string;
    productId: string;

    eligibilities: DioProductEligibility[] = [];
    constraints: DioProductConstraint[] = [];

    constructor(
        private route: ActivatedRoute,
        private typeManager: TypeManagementService,
        private typeUtility: TypeUtilityService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private productsApi: ProductAdminService
    ) { }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');

            this.fetchEligibilities().subscribe();
            this.fetchConstraints().subscribe();
        });
    }

    fetchEligibilities() {
        return this.productsApi.listProductEligibilitys(this.brandId, this.productId).pipe(
            map((eligibilities) => this.eligibilities = eligibilities)
        );
    }
    fetchConstraints() {
        return this.productsApi.listProductConstraints(this.brandId, this.productId).pipe(
            map((constraints) => this.constraints = constraints)
        );
    }

    getEligibilityDetail(eligibility: DioProductEligibility, fieldName: string) {
        if (fieldName === 'LABEL') {
            return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTELIGIBILITYTYPE, eligibility.cdrBanking.eligibilityType.toString());
        }

        if (fieldName === 'VALUE') {
            switch (eligibility.cdrBanking.eligibilityType) {
                case BankingProductEligibilityType.MINAGE: return `Must be at least ${eligibility.cdrBanking.additionalValue} years old.`;
                case BankingProductEligibilityType.MAXAGE: return `Must be under the age of ${eligibility.cdrBanking.additionalValue} years old.`;
                case BankingProductEligibilityType.MININCOME: return `Customer must have an income greater than ${this.typeUtility.convertValueString(eligibility.cdrBanking.additionalValue)}.`;
                case BankingProductEligibilityType.MINTURNOVER: return `Only a business with a turnover greater than ${this.typeUtility.convertValueString(eligibility.cdrBanking.additionalValue)} may apply.`;
                case BankingProductEligibilityType.EMPLOYMENTSTATUS:
                case BankingProductEligibilityType.RESIDENCYSTATUS:
                    return eligibility.cdrBanking.additionalValue;
                default: return '';
            }
        }

        return 'undefined';
    }
    getConstraintDetail(constraint: DioProductConstraint, fieldName) {
        if (fieldName === 'LABEL') {
            return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTCONSTRAINTTYPE, constraint.cdrBanking.constraintType.toString());
        }

        if (fieldName === 'VALUE') {
            const value = this.typeUtility.convertValueString(constraint.cdrBanking.additionalValue);

            switch (constraint.cdrBanking.constraintType) {
                case BankingProductConstraintType.MAXBALANCE:     return `<= ${value}`;
                case BankingProductConstraintType.OPENINGBALANCE: return `At least ${value}`;
                case BankingProductConstraintType.MINBALANCE:     return `>= ${value}`;
                case BankingProductConstraintType.MAXLIMIT:       return `At most ${value}`;
                case BankingProductConstraintType.MINLIMIT:       return `At least ${value}`;
            }
        }

        return 'undefined';
    }

    createEligibility() {
        const ref = this.dialogService.open(ProductEligibilityCreateEditComponent, {
            header: 'Create eligibility',
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

            this.fetchEligibilities().subscribe();
        });
    }
    editEligibility(eligibility: DioProductEligibility) {
        const ref = this.dialogService.open(ProductEligibilityCreateEditComponent, {
            header: 'Edit eligibility',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId, eligibility }
        });

        ref.onClose.subscribe((editedEligibility) => {
            if (!editedEligibility) {
                return;
            }

            this.fetchEligibilities().subscribe();
        });
    }
    removeEligibility(eligibility: DioProductEligibility) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove eligibility "${this.getEligibilityDetail(eligibility, 'LABEL')}"?`,
            header: 'Remove eligibility',
            icon: null,
            accept: () => {
                this.productsApi
                    .deleteProductEligibility(this.brandId, this.productId, eligibility.id)
                    .pipe(switchMap(() => this.fetchEligibilities()))
                    .subscribe();
            },
            reject: () => {}
        });
    }

    createConstraint() {
        const ref = this.dialogService.open(ProductConstraintCreateEditComponent, {
            header: 'Create constraint',
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

            this.fetchConstraints().subscribe();
        });
    }
    editConstraint(constraint: DioProductConstraint) {
        const ref = this.dialogService.open(ProductConstraintCreateEditComponent, {
            header: 'Edit constraint',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId, constraint }
        });

        ref.onClose.subscribe((editedConstraint) => {
            if (!editedConstraint) {
                return;
            }

            this.fetchConstraints().subscribe();
        });
    }
    removeConstraint(constraint: DioProductConstraint) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove eligibility "${this.getConstraintDetail(constraint, 'LABEL')}"?`,
            header: 'Remove constraint',
            icon: null,
            accept: () => {
                this.productsApi
                    .deleteProductConstraint(this.brandId, this.productId, constraint.id)
                    .pipe(switchMap(() => this.fetchConstraints()))
                    .subscribe();
            },
            reject: () => {}
        });
    }

}
