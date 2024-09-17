import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root',
})
export class ProductService {
    private apiUrl = `${environment.apiBaseUrl}/products`;

    constructor(private http: HttpClient) {}

    getAllProducts(): Observable<any[]> {
        return this.http.get<any[]>(this.apiUrl);
    }

    addProduct(product: any): Observable<any> {
        return this.http.post<any>(this.apiUrl, product);
    }

    createProduct(product: any): Observable<any> {
        return this.http.post(this.apiUrl, product);
    }

    updateProduct(product: any): Observable<any> {
        return this.http.put<any>(`${this.apiUrl}/${product.id}`, product);
    }
}

