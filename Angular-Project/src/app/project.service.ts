import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from './project';
import { Fileentity } from './fileentity';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {


  private apiUrl='http://localhost:7070/Test';

  constructor(private http: HttpClient) { }



  savePerson(personData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/`, personData);
  }

  getAllPersons(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/`);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // update(id: string, value: Project): Observable<any>{
  //   return this.http.put(`${this.apiUrl}/${id}`,value);
  // }

  uploadFile(id: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('files', file, file.name);

    return this.http.post(`${this.apiUrl}/${id}`, formData);
  }

  deleteFile(id: string, fileId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}/files/${fileId}`);
  }

  downloadFile(id: string, filename: string): Observable<HttpResponse<Blob>> {
    const url = `${this.apiUrl}/${id}/${filename}`;
    return this.http.get(url, {
      observe: 'response',
      responseType: 'blob',
    });

  

  }

  
  updateFilesForPerson(id: string, formData: FormData): Observable<Fileentity[]> {
    const url = `${this.apiUrl}/${id}/files`;
    return this.http.put<Fileentity[]>(url, formData);
  }

  updatePersonDetails(id: string, updatedPerson: Project): Observable<Project> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<Project>(url, updatedPerson);
  }

  deletePersonAndFilesByFilename(id: string, filename: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}/delete/${filename}`);
  }


}
