import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Analysis } from '../models/analysis';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {

    analysisUrl: string;

    constructor(private http: HttpClient) {
        this.analysisUrl = "http://localhost:8080/analysis"
    }

    public getAllAnalyses(): Observable<Analysis[]> {
        return this.http.get<Analysis[]>(this.analysisUrl).pipe(
          catchError(error => {
            return of([]);
          })
        );
    }

}
