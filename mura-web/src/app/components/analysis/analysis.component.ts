import { Component, OnInit } from '@angular/core';
import { Analysis } from '../../models/analysis';
import { AnalysisService } from '../../services/analysis.service';
import { FileService } from '../../services/file.service';
import { interval } from 'rxjs';

@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

    analyses: Analysis[];

    constructor(private analysisService: AnalysisService, private fileService: FileService) { }

    ngOnInit(): void {
        this.getAllAnalyses();
        interval(10000).subscribe(data => {
          this.getAllAnalyses();
        });
    }

    getAllAnalyses() {
        this.analysisService.getAllAnalyses().subscribe(data => {
            this.analyses = data;
        });
    }

}
