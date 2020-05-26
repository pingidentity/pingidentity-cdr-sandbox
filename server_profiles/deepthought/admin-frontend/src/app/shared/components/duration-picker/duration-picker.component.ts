import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'app-duration-picker',
    templateUrl: './duration-picker.component.html',
    styleUrls: ['./duration-picker.component.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => DurationPickerComponent),
            multi: true
        }
    ]
})
export class DurationPickerComponent implements OnInit, ControlValueAccessor {

    @Input()
    public inputId: string;

    public value: string;

    propagateChange = (_: any) => {};

    constructor() { }

    ngOnInit() {}

    writeValue(value: any) {
        this.value = value;
    }

    registerOnChange(fn) {
        this.propagateChange = fn;
    }

    registerOnTouched() {}

    onChange() {
        this.propagateChange(this.value);
    }

}
