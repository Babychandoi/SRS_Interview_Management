import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Job } from './job.model';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = environment.apiUrl + 'jobs';

  constructor(private http: HttpClient) { }

  // Lấy danh sách công việc với phân trang
  getJobs(query: any) {
    let params = new HttpParams();
    Object.keys(query).forEach(
      key => query[key] && (params = params.append(key, query[key]))
    );
    return this.http.get<any>(this.apiUrl, { params });
  }
  // Tìm kiếm công việc với từ khóa và trạng thái
  searchJobs(keyword: string, status: string, page: number, size: number): Observable<any> {
    let params = new HttpParams();
    if (keyword) {
      params = params.append('keyword', keyword);
    }
    if (status) {
      params = params.append('status', status);
    }
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.post<any>(`${this.apiUrl}/search`, {}, { params });
  }

  // Lấy tất cả công việc
  getAllJobs(): Observable<Job[]> {
    return this.http.get<Job[]>(this.apiUrl);
  }

  // Lấy công việc theo ID
  getJobById(id: string): Observable<any> {
    return this.http.get<Job>(`${this.apiUrl}/${id}`);
  }

  // Tạo công việc mới
  createJob(job: Job): Observable<Job> {
    return this.http.post<Job>(this.apiUrl, job);
  }

  // Cập nhật công việc
  updateJob(job: Job): Observable<Job> {
    return this.http.put<Job>(this.apiUrl, job);
  }

  // Xóa công việc
  deleteJob(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Cập nhật trạng thái công việc
  updateJobStatus(id: string, status: string): Observable<Job> {
    return this.http.put<Job>(`${this.apiUrl}/${id}/status`, { status });
  }
}
