import { FormGroup } from '@angular/forms';
import { CdrFormInput } from '@app/shared/forms/cdr-form-control/cdr-form-control.component';

export class CdrFormGroup extends FormGroup {

    private _submitted = false;

    hideAllControls() {
        Object.keys(this.controls).forEach((key) => (this.controls[key] as CdrFormInput).isVisible = false);
    }

    showControl(controlName: string): void {
        (this.controls[controlName] as CdrFormInput).isVisible = true;
    }

    get submitted() { return this._submitted; };

    setSubmitted(submitted) {
        this._submitted = submitted;
    }

}
