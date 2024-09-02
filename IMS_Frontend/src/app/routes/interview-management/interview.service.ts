
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InterviewService {

  private baseUrl = environment.apiUrl + 'interviews';

  private apiUrl = 'http://localhost:8080/api/interviews'; // Thay đổi URL API của bạn
  private apiUrlDetail = 'http://localhost:8080/api/interviews/details'; // Thay đổi URL API của bạn
  private apiUpdateDetail = "http://localhost:8080/api/interviews/showup";
  private apiSearch = "http://localhost:8080/interviewsSearch";
  private apiAdd = "http://localhost:8080/api/interviews/showadd";
  private apiUrlView = 'http://localhost:8080/api/interviews/view';
  private apisendMail = 'http://localhost:8080/api/interviews/sendEmail';
  private apisend = 'http://localhost:8080/api/interviews/sendEmail';
  constructor(private http: HttpClient) { }

  getInterviews(query: any): Observable<any> {
    let params = new HttpParams();
    Object.keys(query).forEach(
      key => query[key] && (params = params.append(key, query[key]))
    );
    return this.http.get<any>(this.apiUrlView, { params: params });

  }
  send(id: number, param: any): Observable<any> {
    let httpParams = new HttpParams()
      .set('node', param.note)
      .set('result', param.result);

    return this.http.post<any>(`${this.apisend}/${id}`, null, { params: httpParams });
}
  getUpdateInterview(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUpdateDetail}/${id}`);
  }

  getAddInterview(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiAdd}`);
  }

  getInterviewById(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrlDetail}/${id}`);
  }

  addInterview(interview: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, interview);
  }

  updateInterview(interview: any): Observable<any> {
    console.log('Updated interview:', interview);
    return this.http.put<any>(`${this.apiUrl}/${interview.id}`, interview);
  }

  searchInterviews(dataSearch: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.apiSearch}`, dataSearch);
  }
  sendMail(id: number): Observable<any> {
    return this.http.get<any>(`${this.apisendMail}/${id}`);
  }

  getInterviewsByCandidateId(candidateId: any) {
    return this.http.get<any>(`${this.baseUrl}/candidate/${candidateId}`);
  }
}
