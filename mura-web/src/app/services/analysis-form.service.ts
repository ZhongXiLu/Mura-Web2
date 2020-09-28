import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AnalysisForm } from '../models/analysis-form';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnalysisFormService {

    private submissionUrl: string;

    constructor(private http: HttpClient) {
        this.submissionUrl = "http://localhost:8080/submit"
    }

    public submit(form: AnalysisForm) {
        return this.http.post<AnalysisForm>(this.submissionUrl, form);
    }

}
