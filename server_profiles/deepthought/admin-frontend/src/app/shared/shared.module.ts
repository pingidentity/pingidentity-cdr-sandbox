import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NgxPermissionsModule } from 'ngx-permissions';

// PrimeNG
import { ConfirmationService } from 'primeng/api';
import { DynamicDialogModule } from 'primeng/dynamicdialog';

import { AccordionModule }          from 'primeng/accordion';
import { AutoCompleteModule }       from 'primeng/autocomplete';
import { BreadcrumbModule }         from 'primeng/breadcrumb';
import { ButtonModule }             from 'primeng/button';
import { CalendarModule }           from 'primeng/calendar';
import { CardModule }               from 'primeng/card';
import { CarouselModule }           from 'primeng/carousel';
import { ChartModule }              from 'primeng/chart';
import { CheckboxModule }           from 'primeng/checkbox';
import { ChipsModule }              from 'primeng/chips';
import { CodeHighlighterModule }    from 'primeng/codehighlighter';
import { ConfirmDialogModule }      from 'primeng/confirmdialog';
import { ColorPickerModule }        from 'primeng/colorpicker';
import { ContextMenuModule }        from 'primeng/contextmenu';
import { DataViewModule }           from 'primeng/dataview';
import { DialogModule }             from 'primeng/dialog';
import { DropdownModule }           from 'primeng/dropdown';
import { EditorModule }             from 'primeng/editor';
import { FieldsetModule }           from 'primeng/fieldset';
import { FileUploadModule }         from 'primeng/fileupload';
import { FullCalendarModule }       from 'primeng/fullcalendar';
import { GalleriaModule }           from 'primeng/galleria';
import { GrowlModule }              from 'primeng/growl';
import { InplaceModule }            from 'primeng/inplace';
import { InputMaskModule }          from 'primeng/inputmask';
import { InputSwitchModule }        from 'primeng/inputswitch';
import { InputTextModule }          from 'primeng/inputtext';
import { InputTextareaModule }      from 'primeng/inputtextarea';
import { LightboxModule }           from 'primeng/lightbox';
import { ListboxModule }            from 'primeng/listbox';
import { MegaMenuModule }           from 'primeng/megamenu';
import { MenuModule }               from 'primeng/menu';
import { MenubarModule }            from 'primeng/menubar';
import { MessagesModule }           from 'primeng/messages';
import { MessageModule }            from 'primeng/message';
import { MultiSelectModule }        from 'primeng/multiselect';
import { OrderListModule }          from 'primeng/orderlist';
import { OrganizationChartModule }  from 'primeng/organizationchart';
import { OverlayPanelModule }       from 'primeng/overlaypanel';
import { PaginatorModule }          from 'primeng/paginator';
import { PanelModule }              from 'primeng/panel';
import { PanelMenuModule }          from 'primeng/panelmenu';
import { PasswordModule }           from 'primeng/password';
import { PickListModule }           from 'primeng/picklist';
import { ProgressBarModule }        from 'primeng/progressbar';
import { ProgressSpinnerModule }    from 'primeng/progressspinner';
import { RadioButtonModule }        from 'primeng/radiobutton';
import { RatingModule }             from 'primeng/rating';
import { SelectButtonModule }       from 'primeng/selectbutton';
import { SlideMenuModule }          from 'primeng/slidemenu';
import { SliderModule }             from 'primeng/slider';
import { SpinnerModule }            from 'primeng/spinner';
import { SplitButtonModule }        from 'primeng/splitbutton';
import { StepsModule }              from 'primeng/steps';
import { TabMenuModule }            from 'primeng/tabmenu';
import { TableModule }              from 'primeng/table';
import { TabViewModule }            from 'primeng/tabview';
import { TerminalModule }           from 'primeng/terminal';
import { TieredMenuModule }         from 'primeng/tieredmenu';
import { ToastModule }              from 'primeng/toast';
import { ToggleButtonModule }       from 'primeng/togglebutton';
import { ToolbarModule }            from 'primeng/toolbar';
import { TooltipModule }            from 'primeng/tooltip';
import { TreeModule }               from 'primeng/tree';
import { TreeTableModule }          from 'primeng/treetable';
import { VirtualScrollerModule }    from 'primeng/virtualscroller';

// Services

// Pipes
import { DateFormatPipe } from './pipes/date-format.pipe';
import { ConvertFeatureCriteriaPipe } from './pipes/convert-feature-criteria.pipe';
import { PropPathPipe } from './pipes/prop-path.pipe';

// Components
import { CdrFormControlComponent } from './forms/cdr-form-control/cdr-form-control.component';

// Directives
import { VoidDirective } from './directives/void.directive';
import { DurationPickerModule } from 'ngx-duration-picker';
import { DurationPickerComponent } from './components/duration-picker/duration-picker.component';

@NgModule({
    declarations: [
        // Components
        CdrFormControlComponent,

        // Directives
        VoidDirective,

        // Pipes
        DateFormatPipe,
        ConvertFeatureCriteriaPipe,
        PropPathPipe,
        DurationPickerComponent,
    ],
    providers: [
        ConfirmationService
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,

        AccordionModule,
        AutoCompleteModule,
        BreadcrumbModule,
        ButtonModule,
        CalendarModule,
        CardModule,
        CarouselModule,
        ChartModule,
        CheckboxModule,
        ChipsModule,
        CodeHighlighterModule,
        ConfirmDialogModule,
        ColorPickerModule,
        ContextMenuModule,
        DataViewModule,
        DialogModule,
        DynamicDialogModule,
        DropdownModule,
        EditorModule,
        FieldsetModule,
        FileUploadModule,
        FullCalendarModule,
        GalleriaModule,
        GrowlModule,
        InplaceModule,
        InputMaskModule,
        InputSwitchModule,
        InputTextModule,
        InputTextareaModule,
        LightboxModule,
        ListboxModule,
        MegaMenuModule,
        MenuModule,
        MenubarModule,
        MessageModule,
        MessagesModule,
        MultiSelectModule,
        OrderListModule,
        OrganizationChartModule,
        OverlayPanelModule,
        PaginatorModule,
        PanelModule,
        PanelMenuModule,
        PasswordModule,
        PickListModule,
        ProgressBarModule,
        ProgressSpinnerModule,
        RadioButtonModule,
        RatingModule,
        SelectButtonModule,
        SlideMenuModule,
        SliderModule,
        SpinnerModule,
        SplitButtonModule,
        StepsModule,
        TableModule,
        TabMenuModule,
        TabViewModule,
        TerminalModule,
        TieredMenuModule,
        ToastModule,
        ToggleButtonModule,
        ToolbarModule,
        TooltipModule,
        TreeModule,
        TreeTableModule,
        VirtualScrollerModule,
        DurationPickerModule,

        RouterModule,
        NgxPermissionsModule.forRoot(),
    ],
    exports: [
        AccordionModule,
        ButtonModule,
        CalendarModule,
        ChartModule,
        CheckboxModule,
        ConfirmDialogModule,
        DataViewModule,
        DynamicDialogModule,
        DropdownModule,
        FieldsetModule,
        InputTextModule,
        InputTextareaModule,
        LightboxModule,
        ListboxModule,
        MessageModule,
        MessagesModule,
        OverlayPanelModule,
        PanelModule,
        ProgressBarModule,
        ProgressSpinnerModule,
        SpinnerModule,
        SplitButtonModule,
        TableModule,
        TabViewModule,
        ToastModule,
        ToolbarModule,
        TooltipModule,
        TieredMenuModule,

        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        NgxPermissionsModule,

        DurationPickerModule,

        // Components
        CdrFormControlComponent,
        DurationPickerComponent,

        // Directives
        VoidDirective,

        // Pipes
        DateFormatPipe,
        ConvertFeatureCriteriaPipe,
        PropPathPipe
    ]
})
// @ts-ignore
export class SharedModule { }
