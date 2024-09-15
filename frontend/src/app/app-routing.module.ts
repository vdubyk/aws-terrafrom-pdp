import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OrdersComponent } from './modules/components/orders/orders.component';
import { ProductsComponent } from './modules/components/products/products.component';
import { InstanceTestingComponent } from './modules/components/instance-testing/instance-testing.component';
import {MainComponent} from "./modules/components/main/main.component";

const routes: Routes = [
    {
        path: '', component: MainComponent, children: [
            { path: 'products', component: ProductsComponent },
            { path: 'orders', component: OrdersComponent },
            { path: 'instance-testing', component: InstanceTestingComponent },
        ]
    },
    { path: '**', redirectTo: '' } // Catch-all route
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
