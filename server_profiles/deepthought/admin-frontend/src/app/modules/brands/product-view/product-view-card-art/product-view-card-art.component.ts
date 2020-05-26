import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ConfirmationService, DialogService } from 'primeng/api';
import { ProductAdminService, DioProductCardArt } from '@bizaoss/deepthought-admin-angular-client';
import { map, switchMap } from 'rxjs/operators';
import { ProductEligibilityCreateEditComponent } from '../product-view-constraints/product-eligibility-create-edit/product-eligibility-create-edit.component';
import { ProductCardArtCreateEditComponent } from './product-card-art-create-edit/product-card-art-create-edit.component';

@Component({
    selector: 'app-product-view-card-art',
    templateUrl: './product-view-card-art.component.html',
    styles: [`
        .title p-button {
            float: right;
        }
        
        .actions {
            text-align: right;
        }
        
        .actions p-button {
            margin-left: 5px;
        }
    `]
})
export class ProductViewCardArtComponent implements OnInit {

    brandId: string;
    productId: string;

    cardArts: DioProductCardArt[] = [];

    modalWidth = '40%';
    modalStyles = {
        'overflow-y': 'auto',
        'overflow-x': 'hidden',
        'max-height': '80vh',
        'min-height': '250px'
    };

    constructor(
        private route: ActivatedRoute,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService,
        private productsApi: ProductAdminService
    ) { }

    ngOnInit() {
        this.route.parent.paramMap.subscribe((params: ParamMap) => {
            this.brandId = params.get('brandId');
            this.productId = params.get('productId');

            this.fetchCardArt().subscribe();
        });
    }

    fetchCardArt() {
        return this.productsApi.listProductCardArts(this.brandId, this.productId).pipe(
            map((cardArts) => this.cardArts = cardArts)
        );
    }

    createCardArt() {
        const ref = this.dialogService.open(ProductCardArtCreateEditComponent, {
            header: 'Create card art',
            width: this.modalWidth,
            style: this.modalStyles,
            data: { brandId: this.brandId, productId: this.productId }
        });

        ref.onClose.subscribe((_cardArt) => _cardArt ? this.fetchCardArt().subscribe() : void 0);
    }

    editCardArt(cardArt: DioProductCardArt) {
        const ref = this.dialogService.open(ProductCardArtCreateEditComponent, {
            header: 'Edit card art',
            width: this.modalWidth,
            style: this.modalStyles,
            data: { brandId: this.brandId, productId: this.productId, cardArt }
        });

        ref.onClose.subscribe((_cardArt) => _cardArt ? this.fetchCardArt().subscribe() : void 0);
    }

    removeCardArt(cardArt: DioProductCardArt) {
        this.confirmationService.confirm({
            message: `Are you sure want to remove this card art?`,
            header: 'Remove card art',
            icon: null,
            accept: () => {
                this.productsApi
                    .deleteProductCardArt(this.brandId, this.productId, cardArt.id)
                    .pipe(switchMap(() => this.fetchCardArt()))
                    .subscribe();
            },
            reject: () => {}
        });
    }

}
