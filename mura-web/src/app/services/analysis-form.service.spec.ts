import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { AnalysisFormService } from './analysis-form.service';

describe('AnalysisFormService', () => {
  let service: AnalysisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientModule]});
    service = TestBed.inject(AnalysisFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
