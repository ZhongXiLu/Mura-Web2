import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class FileService {

      private fileUrl: string;

      constructor(private http: HttpClient) {
          this.fileUrl = "http://localhost:8080/analysis"
      }

      public getFile(repoName: string, fileName: string) {
          this.http.get(this.fileUrl + "/" + repoName + "/" + fileName, {responseType: 'blob'}).subscribe(file =>
            saveAs(file, fileName)
          );
      }
}
