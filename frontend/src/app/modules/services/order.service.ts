import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/envirement";

@Injectable({
    providedIn: 'root',
})
export class OrderService {
    private apiUrl = `${environment.apiBaseUrl}/orders`;

    constructor(private http: HttpClient) {}

    getAllOrders(): Observable<any[]> {
        return this.http.get<any[]>(this.apiUrl);
    }

    createOrder(order: any): Observable<any> {
        return this.http.post(this.apiUrl, order);
    }
}

