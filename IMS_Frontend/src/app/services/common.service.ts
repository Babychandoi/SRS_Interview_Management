import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  private baseUrl = environment.apiUrl + 'commons';

  constructor(private http: HttpClient) { }

  getDepartments() {
    return this.http.get(`${this.baseUrl}/departments`);
  }

  getOfferStatuses() {
    return this.http.get(`${this.baseUrl}/offer-statuses`);
  }

  getPositions() {
    return this.http.get(`${this.baseUrl}/positions`);
  }

  getContractTypes() {
    return this.http.get(`${this.baseUrl}/contract-types`);
  }

  getPositionLevels() {
    return this.http.get(`${this.baseUrl}/position-levels`);
  }

  getJobStatuses() {
    return this.http.get(`${this.baseUrl}/job-statuses`);
  }

  getCandidateStatuses() {
    return this.http.get(`${this.baseUrl}/candidate-statuses`);
  }
}
