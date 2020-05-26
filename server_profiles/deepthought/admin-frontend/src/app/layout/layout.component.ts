import { Component, OnDestroy, OnInit, NgZone, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { LeftSidebarService } from '@app/core/services/left-sidebar.service';
import { NavigationStart, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { LayoutService } from '@app/layout/layout.service';
import { ConfirmationService } from 'primeng/api';

@Component({
    selector: 'layout',
    templateUrl: './layout.component.html',
    styleUrls: ['layout.component.scss'],
    providers: [ConfirmationService],
    animations: [
        trigger('mask-anim', [
            state('void', style({
                opacity: 0
            })),
            state('visible', style({
                opacity: 0.8
            })),
            transition('* => *', animate('250ms cubic-bezier(0, 0, 0.2, 1)'))
        ])
    ]
})
export class LayoutComponent implements OnInit, OnDestroy, AfterViewChecked {

    menu = [
        {
            label: 'Brands',
            icon: 'dashboard',
            routerLink: '/brands',
            permissions: null,
        },
        // {
        //     label: 'Bundles',
        //     icon: 'map',
        //     routerLink: '/bundles',
        //     permissions: null
        // },
    ];

    showPageLoader = false;

    // *****************************************************************************************************************

    menuClick: boolean;

    userMenuClick: boolean;

    topbarUserMenuActive: boolean;

    horizontal = true;

    menuActive: boolean;

    rippleInitListener: any;

    rippleMouseDownListener: any;

    menuHoverActive: boolean;

    resetMenu: boolean;

    topbarColor = 'layout-topbar-blue';

    menuColor = 'layout-menu-light';

    themeColor = 'blue';

    layoutColor = 'blue';

    topbarSize = 'large';

    configDialogActive: boolean;

    leftSidebarRef: any = null;

    isBurgerOpen = false;

    constructor(
        private zone: NgZone,
        private leftSidebar: LeftSidebarService,
        private router: Router,
        private layoutService: LayoutService,
        private cdRef: ChangeDetectorRef
    ) {}

    ngOnInit() {
        this.zone.runOutsideAngular(() => this.bindRipple());

        this.layoutService.togglePageLoader
            .subscribe((showPageLoader) => this.showPageLoader = showPageLoader);

        this.leftSidebar.toggleSidebar
            .subscribe((ref) => this.leftSidebarRef = ref);

        this.leftSidebar.toggleBurgers
            .subscribe(() => this.isBurgerOpen = !this.isBurgerOpen);

        this.router.events
            .pipe(filter(event => event instanceof NavigationStart))
            .subscribe(() => this.leftSidebar.toggleSidebar.emit(null));
    }

    ngAfterViewChecked() {
        this.cdRef.detectChanges();
    }

    bindRipple() {
        this.rippleInitListener = this.init.bind(this);
        document.addEventListener('DOMContentLoaded', this.rippleInitListener);
    }

    init() {
        this.rippleMouseDownListener = this.rippleMouseDown.bind(this);
        document.addEventListener('mousedown', this.rippleMouseDownListener, false);
    }

    rippleMouseDown(e) {
        const parentNode = 'parentNode';
        for (let target = e.target; target && target !== this; target = target[parentNode]) {
            if (!this.isVisible(target)) {
                continue;
            }

            // Element.matches() -> https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
            if (this.selectorMatches(target, '.ripplelink, .ui-button, .ui-listbox-item, .ui-multiselect-item, .ui-fieldset-toggler')) {
                const element = target;
                this.rippleEffect(element, e);
                break;
            }
        }
    }

    selectorMatches(el, selector) {
        const matches = 'matches';
        const webkitMatchesSelector = 'webkitMatchesSelector';
        const mozMatchesSelector = 'mozMatchesSelector';
        const msMatchesSelector = 'msMatchesSelector';
        const p = Element.prototype;
        const f = p[matches] || p[webkitMatchesSelector] || p[mozMatchesSelector] || p[msMatchesSelector] || function(s) {
            return [].indexOf.call(document.querySelectorAll(s), this) !== -1;
        };
        return f.call(el, selector);
    }

    isVisible(el) {
        return !!(el.offsetWidth || el.offsetHeight);
    }

    rippleEffect(element, e) {
        if (element.querySelector('.ink') === null) {
            const inkEl = document.createElement('span');
            this.addClass(inkEl, 'ink');

            if (this.hasClass(element, 'ripplelink') && element.querySelector('span')) {
                element.querySelector('span').insertAdjacentHTML('afterend', '<span class=\'ink\'></span>');
            } else {
                element.appendChild(inkEl);
            }
        }

        const ink = element.querySelector('.ink');
        this.removeClass(ink, 'ripple-animate');

        if (!ink.offsetHeight && !ink.offsetWidth) {
            const d = Math.max(element.offsetWidth, element.offsetHeight);
            ink.style.height = d + 'px';
            ink.style.width = d + 'px';
        }

        const x = e.pageX - this.getOffset(element).left - (ink.offsetWidth / 2);
        const y = e.pageY - this.getOffset(element).top - (ink.offsetHeight / 2);

        ink.style.top = y + 'px';
        ink.style.left = x + 'px';
        ink.style.pointerEvents = 'none';
        this.addClass(ink, 'ripple-animate');
    }

    hasClass(element, className) {
        if (element.classList) {
            return element.classList.contains(className);
        } else {
            return new RegExp('(^| )' + className + '( |$)', 'gi').test(element.className);
        }
    }

    addClass(element, className) {
        if (element.classList) {
            element.classList.add(className);
        } else {
            element.className += ' ' + className;
        }
    }

    removeClass(element, className) {
        if (element.classList) {
            element.classList.remove(className);
        } else {
            element.className = element.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
        }
    }

    getOffset(el) {
        const rect = el.getBoundingClientRect();

        return {
            top: rect.top + (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0),
            left: rect.left + (window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0),
        };
    }

    unbindRipple() {
        if (this.rippleInitListener) {
            document.removeEventListener('DOMContentLoaded', this.rippleInitListener);
        }
        if (this.rippleMouseDownListener) {
            document.removeEventListener('mousedown', this.rippleMouseDownListener);
        }
    }

    blockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.add('blocked-scroll');
        } else {
            document.body.className += ' blocked-scroll';
        }
    }

    unblockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.remove('blocked-scroll');
        } else {
            document.body.className = document.body.className.replace(new RegExp('(^|\\b)' +
                'blocked-scroll'.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
        }
    }

    ngOnDestroy() {
        this.unbindRipple();
    }

    onWrapperClick() {
        if (!this.menuClick) {
            this.menuActive = false;
        }

        if (!this.userMenuClick) {
            this.topbarUserMenuActive = false;
        }

        if (!this.menuClick) {
            if (this.horizontal) {
                this.resetMenu = true;
            }

            this.menuHoverActive = false;
            this.unblockBodyScroll();
        }

        this.userMenuClick = false;
        this.menuClick = false;
    }

    onMenuButtonClick(event: Event) {
        this.menuClick = true;

        if (!this.horizontal || this.isMobile()) {
            this.menuActive = !this.menuActive;

            if (this.menuActive) {
                this.blockBodyScroll();
            } else {
                this.unblockBodyScroll();
            }
        }

        event.preventDefault();
    }

    onTopbarUserMenuButtonClick(event) {
        this.userMenuClick = true;
        this.topbarUserMenuActive = !this.topbarUserMenuActive;

        event.preventDefault();
    }

    onTopbarUserMenuClick(event) {
        this.userMenuClick = true;

        if (event.target.nodeName === 'A' || event.target.parentNode.nodeName === 'A') {
            this.topbarUserMenuActive = false;
        }
    }

    onTopbarSubItemClick(event) {
        event.preventDefault();
        this.configDialogActive = true;
    }

    onSidebarClick(event: Event) {
        this.menuClick = true;
        this.resetMenu = false;
    }

    isMobile() {
        return window.innerWidth <= 1024;
    }

    isTablet() {
        const width = window.innerWidth;
        return width <= 1024 && width > 640;
    }

    changeTopbarTheme(event, color) {
        this.topbarColor = 'layout-topbar-' + color;

        event.preventDefault();
    }

    changeMenuToHorizontal(event, mode) {
        this.horizontal = mode;

        event.preventDefault();
    }

    changeMenuTheme(event, color) {
        this.menuColor = 'layout-menu-' + color;

        event.preventDefault();
    }

    changeComponentTheme(event, theme) {
        this.themeColor = theme;
        const themeLink: HTMLLinkElement = document.getElementById('theme-css') as HTMLLinkElement;
        themeLink.href = 'assets/theme/' + 'theme-' + theme + '.css';

        event.preventDefault();
    }

    changePrimaryColor(event, color) {
        this.layoutColor = color;
        const layoutLink: HTMLLinkElement = document.getElementById('layout-css') as HTMLLinkElement;
        layoutLink.href = 'assets/layout/css/layout-' + color + '.css';

        event.preventDefault();
    }
}
