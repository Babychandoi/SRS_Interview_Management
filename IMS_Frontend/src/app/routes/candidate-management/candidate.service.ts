import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Candidate } from './candidate.model';
import { JobDetailsDTO } from './candidate.model';

@Injectable({
  providedIn: 'root'
})
export class CandidateService {

  private baseUrl = environment.apiUrl + 'candidates';

  constructor(private http: HttpClient) { }

  getAllCandidates(query: any) {
    let params = new HttpParams();
    Object.keys(query).forEach(
      key => query[key] && (params = params.append(key, query[key]))
    );
    return this.http.get<any>(this.baseUrl, { params: params });
  }

  getAllNotBannedCandidates() {
    return this.http.get<any>(`${this.baseUrl}/not-banned`);
  }

  getCandidateById(id: number): Observable<Candidate> {
    return this.http.get<Candidate>(`${this.baseUrl}/${id}`);
  }

  createCandidate(candidate: any, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('candidate', JSON.stringify(candidate));
    formData.append('cv', file);

    return this.http.post<any>(this.baseUrl, formData);
  }
  uploadCv(formData: FormData): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/files/extract', formData); // Điều chỉnh URL theo backend của bạn
  }


  updateCandidate(id: number, candidate: any, file?: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('candidate', JSON.stringify(candidate));
    if (file) {
      formData.append('cv', file);
    }

    return this.http.put<any>(`${this.baseUrl}/${id}`, formData);
  }

  deleteCandidate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  banCandidate(id: number): Observable<Candidate> {
    return this.http.put<Candidate>(`${this.baseUrl}/${id}/ban`, {});
  }
  
  getJobsByCandidateId(candidateId: number): Observable<JobDetailsDTO[]> {
    return this.http.get<JobDetailsDTO[]>(`${this.baseUrl}/${candidateId}/jobs`);
  }

}
