import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AnalysisComponent } from './components/analysis/analysis.component';
import { AnalysisFormComponent } from './components/analysis-form/analysis-form.component';

const routes: Routes = [
    { path: '', component: AnalysisComponent },
    { path: 'submit', component: AnalysisFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
