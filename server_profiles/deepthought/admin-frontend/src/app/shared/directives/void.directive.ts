import { Directive, HostBinding, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Directive({
    selector: '[void]'
})
export class VoidDirective implements OnInit {

    @HostBinding('attr.href')
    public href;

    constructor(private sanitizer: DomSanitizer) { }

    ngOnInit(): void {
        this.href = this.sanitizer.bypassSecurityTrustUrl('javascript: void(0)');
    }

}
