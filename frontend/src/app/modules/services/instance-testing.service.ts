import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/envirement";

@Injectable({
    providedIn: 'root',
})
export class InstanceTestingService {
    private apiUrl = `${environment.apiBaseUrl}/instance`;

    constructor(private http: HttpClient) {}

    getPublicIp(): Observable<string> {
        return this.http.get<string>(`${this.apiUrl}/public-ip`);
    }

    getPrivateIp(): Observable<string> {
        return this.http.get<string>(`${this.apiUrl}/private-ip`);
    }
}

