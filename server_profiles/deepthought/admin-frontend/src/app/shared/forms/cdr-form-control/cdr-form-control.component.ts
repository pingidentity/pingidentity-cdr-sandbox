import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { SelectItem } from 'primeng/api';

export enum CdrFormControlTypes {
    INPUT = 'input',
    SELECT = 'select',
    TEXTAREA = 'textarea',
    DATE = 'date',
    CHECKBOX = 'checkbox',
    DURATION = 'duration',
}

export class CdrFormControl extends FormControl {

    type: CdrFormControlTypes;
    label: string;
    isVisible = true;

    constructor(type: CdrFormControlTypes, defaultValue, label: string, validators = [], asyncValidators = []) {
        super(defaultValue, validators, asyncValidators);

        this.type = type;
        this.label = label;
    }
}
export class CdrFormInput extends CdrFormControl {

    subType: string;

    constructor(defaultValue, label: string, validators = [], subType = 'text', asyncValidators = []) {
        super(CdrFormControlTypes.INPUT, defaultValue, label, validators, asyncValidators);

        this.subType = subType;
    }
}
export class CdrFormSelect extends CdrFormControl {

    options: SelectItem[];

    constructor(defaultValue, label: string, validators = [], options: SelectItem[] = [], asyncValidators = []) {
        super(CdrFormControlTypes.SELECT, defaultValue, label, validators, asyncValidators);

        this.options = options;
    }
}
export class CdrFormTextarea extends CdrFormControl {

    constructor(defaultValue, label: string, validators = [], asyncValidators = []) {
        super(CdrFormControlTypes.TEXTAREA, defaultValue, label, validators, asyncValidators);
    }
}
export class CdrFormDate extends CdrFormControl {

    constructor(defaultValue, label: string, validators = [], asyncValidators = []) {
        super(CdrFormControlTypes.DATE, defaultValue, label, validators, asyncValidators);
    }
}
export class CdrFormCheckbox extends CdrFormControl {

    constructor(defaultValue, label: string, validators = [], asyncValidators = []) {
        super(CdrFormControlTypes.CHECKBOX, defaultValue, label, validators, asyncValidators);
    }
}
export class CdrFormDuration extends CdrFormControl {

    constructor(defaultValue, label: string, validators = [], asyncValidators = []) {
        super(CdrFormControlTypes.DURATION, defaultValue, label, validators, asyncValidators);
    }
}

type FormControlType = CdrFormInput & CdrFormSelect & CdrFormTextarea & CdrFormDate & CdrFormCheckbox & AbstractControl & FormGroup;

@Component({
    selector: 'cdr-form-control',
    templateUrl: './cdr-form-control.component.html',
    styleUrls: ['./cdr-form-control.component.scss']
})
export class CdrFormControlComponent implements OnInit {

    @Input()
    public control: FormControlType;

    @Input()
    public showErrors = false;

    @Input()
    public showLabel = true;

    public idForLabel = `${Date.now()}${Math.round(Math.random() * 10000)}`;

    public originalOrder = ((): number => 0);

    constructor() { }

    ngOnInit() {}

    hasRequiredValidator(abstractControl: AbstractControl): boolean {
        if (abstractControl.validator) {
            const validator = abstractControl.validator({}as AbstractControl);
            if (validator && validator.required) {
                return true;
            }
        }
        return false;
    }

    getFirstError(control: CdrFormControl): string {
        if (control && control.errors) {
            const errors = Object.keys(control.errors);

            for (const key of errors) {
                switch (key) {
                    case 'required': return `${control.label} is required field`;
                    case 'uuid': return `${control.label} must have UUID format`;
                    case 'SERVER': return control.errors[key];
                }
            }
        }

        return '';
    }

}
