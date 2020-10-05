import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AnalysisService } from './analysis.service';

const mockResponse = [
  {
    "id": 1,
    "repoName": "slub/urnlib",
    "gitRepo": "https://github.com/slub/urnlib",
    "report": "report.html",
    "mutants": "mutants.csv",
    "startTime": "2020-10-01 09:08 GMT",
    "finished": true,
    "successful": true,
    "errorMessage": ""
  }
]

describe('AnalysisService', () => {
  let httpTestingController: HttpTestingController;
  let service: AnalysisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ AnalysisService ]
    });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(AnalysisService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all analyses', () => {
    service.getAllAnalyses().subscribe(
      res => expect(res).toEqual(mockResponse, 'should return a list of all the analyses'),
      fail
    );

    httpTestingController.expectOne(service.analysisUrl).flush(mockResponse);
  });

  it('should return empty list if failed contacting backend', () => {
    service.getAllAnalyses().subscribe(
      res => expect(res.length).toEqual(0, 'should return an empty list'),
      fail
    );

    httpTestingController.expectOne(service.analysisUrl).flush("505 Error", {status: 500, statusText: 'Internal Server Error'});
  });

});
