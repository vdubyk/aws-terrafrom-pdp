import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, Event as RouterEvent } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { filter } from 'rxjs/operators';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

    items!: MenuItem[];
    activeItem!: MenuItem;

    constructor(private router: Router) {}

    ngOnInit() {
        this.items = [
            { label: 'Products', icon: 'pi pi-fw pi-list', routerLink: ['/products'] },
            { label: 'Orders', icon: 'pi pi-fw pi-shopping-cart', routerLink: ['/orders'] },
            { label: 'Instance Testing', icon: 'pi pi-fw pi-desktop', routerLink: ['/instance-testing'] }
        ];

        // Subscribe to router events and set active item based on URL
        this.router.events.pipe(
            filter((event: RouterEvent): event is NavigationEnd => event instanceof NavigationEnd)
        ).subscribe((event: NavigationEnd) => {
            this.updateActiveItem(event.urlAfterRedirects);
        });

        // Initial active item based on current route
        this.updateActiveItem(this.router.url);
    }

    onTabChange(event: MenuItem) {
        this.activeItem = event;
    }

    private updateActiveItem(url: string) {
        // Find the item based on the URL
        this.activeItem = this.items.find(item => url.includes(item.routerLink![0])) || this.items[0];
    }
}
