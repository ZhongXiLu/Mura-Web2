import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AnalysisComponent } from './components/analysis/analysis.component';
import { AnalysisService } from './services/analysis.service';
import { AnalysisFormComponent } from './components/analysis-form/analysis-form.component';

@NgModule({
  declarations: [
    AppComponent,
    AnalysisComponent,
    AnalysisFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [AnalysisService],
  bootstrap: [AppComponent]
})
export class AppModule { }
