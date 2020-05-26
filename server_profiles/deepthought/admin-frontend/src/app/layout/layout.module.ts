import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@app/shared/shared.module';
import { BreadcrumbService } from './breadcrumb.service';
import { LayoutComponent } from './layout.component';
import { LayoutService } from '@app/layout/layout.service';
import { HeaderComponent } from '@app/layout/header/header.component';

@NgModule({
    declarations: [
        LayoutComponent,
        HeaderComponent
    ],
    providers: [
        BreadcrumbService,
        LayoutService
    ],
    imports: [
        CommonModule,
        SharedModule,
    ],
    exports: [
        LayoutComponent
    ]
})
export class LayoutModule { }
