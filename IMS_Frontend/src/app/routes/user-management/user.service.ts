import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = `${environment.apiUrl}users`;

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  getRecruiters(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '/recruiters');
  }

  getInterviewers(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '/interviewers');
  }

}
