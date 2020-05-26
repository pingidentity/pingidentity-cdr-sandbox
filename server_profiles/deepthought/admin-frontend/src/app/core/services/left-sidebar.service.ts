import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class LeftSidebarService {

    public toggleSidebar: EventEmitter<any> = new EventEmitter();

    public toggleBurgers: EventEmitter<any> = new EventEmitter();

    constructor() { }
}
