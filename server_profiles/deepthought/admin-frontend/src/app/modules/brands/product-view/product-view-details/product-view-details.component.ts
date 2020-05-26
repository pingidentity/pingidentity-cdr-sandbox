import { Component, OnInit } from '@angular/core';
import {
    BundleAdminService,
    DioProduct,
    DioProductBundle,
    FormFieldType,
    ProductAdminService
} from '@bizaoss/deepthought-admin-angular-client';
import { DateFormatPipe } from '@app/shared/pipes/date-format.pipe';
import { TypeManagementService } from '@app/core/services/type-management.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { map, switchMap } from 'rxjs/operators';
import { ConfirmationService, DialogService } from 'primeng/api';
import { ProductAddBundleComponent } from './product-add-bundle/product-add-bundle.component';

interface IDetailsProp {
    key: string;
    label: string;
}

@Component({
    selector: 'app-product-view-details',
    templateUrl: './product-view-details.component.html',
    styleUrls: ['./product-view-details.component.scss'],
    providers: [DateFormatPipe]
})
export class ProductViewDetailsComponent implements OnInit {

    brandId: string;
    productId: string;

    product: DioProduct;

    summaryDetails: IDetailsProp[] = [
        { key: 'id', label: 'Product ID' },
        { key: 'name', label: 'Name' },
        { key: 'description', label: 'Description' },
        { key: 'cdrBanking.productCategory', label: 'Product Category' },
        { key: 'cdrBanking.tailored', label: 'Tailored Product' },
        { key: 'cdrBanking.effective', label: 'Effective Date' },
        { key: 'cdrBanking.lastUpdated', label: 'Last Updated' },
        { key: 'cdrBanking.applicationUri', label: 'Application URI' },
    ];

    additionalInformationDetails: IDetailsProp[] = [
        { key: 'overviewUri', label: 'Overview URI' },
        { key: 'termsUri', label: 'Terms URI' },
        { key: 'eligibilityUri', label: 'Eligibility URI' },
        { key: 'feesAndPricingUri', label: 'Fees and pricing URI' },
        { key: 'bundleUri', label: 'Bundle URI' },
    ];

    bundles: DioProductBundle[];

    constructor(
        private route: ActivatedRoute,
        private dateFormatPipe: DateFormatPipe,
        private typeManager: TypeManagementService,
        private productApi: ProductAdminService,
        private bundleApi: BundleAdminService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
    ) { }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');

            this.fetchProductBundles().subscribe();
        });
    }

    fetchProductBundles() {
        return this.productApi.listBundlesForProduct(this.brandId, this.productId).pipe(
            map((bundles) => this.bundles = bundles)
        );
    }

    setProduct(product: DioProduct) {
        this.product = product;
    }

    getEffectiveDate(from, to) {
        let effectiveDate = `${ from ? this.dateFormatPipe.transform(from) : '-' }`;

        if (from) {
            effectiveDate += ` - ${ to && !to.startsWith('9999') ? this.dateFormatPipe.transform(to) : 'Present' }`;
        }

        return effectiveDate;
    }

    getProductCategory(category) {
        return this.typeManager.getLabel(FormFieldType.BANKINGPRODUCTCATEGORY, category);
    }

    addBundleToProduct() {
        const ref = this.dialogService.open(ProductAddBundleComponent, {
            header: 'Add bundle to product',
            width: '40%',
            style: {
                'overflow-y': 'auto',
                'overflow-x': 'hidden',
                'max-height': '80vh',
                'min-height': '250px'
            },
            data: { brandId: this.brandId, productId: this.productId }
        });

        ref.onClose.subscribe((isAdded) => isAdded ? this.fetchProductBundles().subscribe() : void(0));
    }

    removeBundleFromProduct(bundle: DioProductBundle) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove bundle "${bundle.name}" from this product?`,
            header: 'Remove bundle',
            icon: null,
            accept: () => {
                this.bundleApi
                    .deleteProductFromProductBundle(this.brandId, bundle.id, this.productId)
                    .pipe(switchMap(() => this.fetchProductBundles()))
                    .subscribe();
            },
            reject: () => {}
        });
    }

}
