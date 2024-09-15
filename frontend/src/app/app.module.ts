import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './modules/components/products/products.component';
import { OrdersComponent } from './modules/components/orders/orders.component';
import { InstanceTestingComponent } from './modules/components/instance-testing/instance-testing.component';
import { MainComponent } from './modules/components/main/main.component';
import {RouterModule} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {TabMenuModule} from "primeng/tabmenu";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DropdownModule} from "primeng/dropdown";
import {InputNumberModule} from "primeng/inputnumber";
import {DividerModule} from "primeng/divider";

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    OrdersComponent,
    InstanceTestingComponent,
    MainComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        RouterModule,
        HttpClientModule,
        TabMenuModule,
        TableModule,
        ButtonModule,
        DialogModule,
        FormsModule,
        BrowserAnimationsModule,
        InputTextModule,
        ConfirmDialogModule,
        ReactiveFormsModule,
        DropdownModule,
        InputNumberModule,
        DividerModule
    ],
    providers: [ConfirmationService],  // <-- Add ConfirmationService here
  bootstrap: [AppComponent]
})
export class AppModule { }
