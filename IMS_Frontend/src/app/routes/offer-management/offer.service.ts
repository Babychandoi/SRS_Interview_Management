import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OfferService {

  private baseUrl = environment.apiUrl + 'offers';


  constructor(private http: HttpClient) { }


  getOffers(query: any) {
    let params = new HttpParams();
    Object.keys(query).forEach(
      key => query[key] && (params = params.append(key, query[key]))
    );
    return this.http.get<any>(`${this.baseUrl}`, { params: params });
  }

  getOfferById(id: number) {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  addOffer(offer: any) {
    console.log(offer);
    return this.http.post<any>(this.baseUrl, offer);
  }

  updateOffer(offer: any) {
    console.log(offer);
    return this.http.put<any>(this.baseUrl, offer);
  }

  exportOffers(exportData: any) {
    console.log(exportData);
    return this.http.post<any>(`${this.baseUrl}/export`, exportData, { responseType: 'blob' as 'json' });
  }

  cancelOffer(id: number) {
    return this.http.put<any>(`${this.baseUrl}/cancel/${id}`, {});
  }

  approveOffer(id: number, flag: boolean) {
    return this.http.put<any>(`${this.baseUrl}/approve/${id}?flag=${flag}`, {});
  }

  acceptOffer(id: number, flag: boolean) {
    return this.http.put<any>(`${this.baseUrl}/accept/${id}?flag=${flag}`, {});
  }

  markAsSentOffer(id: number) {
    return this.http.put<any>(`${this.baseUrl}/mark-as-sent/${id}`, {});
  }
}
