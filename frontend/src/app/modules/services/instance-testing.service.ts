import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root',
})
export class InstanceTestingService {
    private apiUrl = `${environment.apiBaseUrl}/instance`;

    constructor(private http: HttpClient) {}

    getPublicIp(): Observable<string> {
        return this.http.get(`${this.apiUrl}/public-ip`, { responseType: 'text' });
    }

    getPrivateIp(): Observable<string> {
        return this.http.get(`${this.apiUrl}/private-ip`, { responseType: 'text' });
    }
}

