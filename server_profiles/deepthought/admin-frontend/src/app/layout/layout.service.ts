import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

    togglePageLoader: EventEmitter<boolean> = new EventEmitter<boolean>();

    constructor() { }
}
