import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { AppComponent } from '../../app.component';

@Component({
  selector: 'page-not-found',
  templateUrl: './page.not-found.component.html',
})
export class PageNotFoundComponent implements OnInit {

    constructor(
        private router: Router,
        private appComponent: AppComponent,
        private location: Location
    ) {}

    ngOnInit() {
        this.location.replaceState(this.appComponent.previousUrl);
    }
}
