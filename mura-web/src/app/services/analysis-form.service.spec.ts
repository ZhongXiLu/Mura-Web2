import { TestBed } from '@angular/core/testing';

import { AnalysisFormService } from './analysis-form.service';

describe('AnalysisFormService', () => {
  let service: AnalysisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnalysisFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
