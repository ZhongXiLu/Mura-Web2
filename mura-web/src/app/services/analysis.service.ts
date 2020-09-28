import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Analysis } from '../models/analysis';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {

    private analysisUrl: string;

    constructor(private http: HttpClient) {
        this.analysisUrl = "http://localhost:8080/analysis"
    }

    public getAllAnalyses(): Observable<Analysis[]> {
        return this.http.get<Analysis[]>(this.analysisUrl);
    }

}
