import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnalysisForm } from '../../models/analysis-form';
import { AnalysisFormService } from '../../services/analysis-form.service';

@Component({
  selector: 'app-analysis-form',
  templateUrl: './analysis-form.component.html',
  styleUrls: ['./analysis-form.component.css']
})
export class AnalysisFormComponent implements OnInit {

    analysisForm: AnalysisForm = new AnalysisForm();

    constructor(private route: ActivatedRoute, private router: Router, private analysisFormService: AnalysisFormService) {}

    ngOnInit(): void {}

    submit() {
        this.analysisFormService.submit(this.analysisForm).subscribe(
            (data) => {
              this.router.navigate(['']);
            }
        );
    }

}
