import { Component, OnInit, ViewChild } from '@angular/core';
import { InterviewService } from '../interview.service';
import { Router } from '@angular/router';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { UserService } from '../../user-management/user.service';

@Component({
  selector: 'app-interview-list',
  templateUrl: './interview-list.component.html',
  styleUrls: ['./interview-list.component.css']
})
export class InterviewListComponent implements OnInit {

  query = {
    keyword: null,
    page: 0,
    status: null,
    interviewerId: null,
  };

  list: any[] = [];
  interviewers: any[] = [];

  pageEvent!: PageEvent;
  length = 0; // total number of offers
  pageSize = 0; // number of offers per page
  pageIndex = 0; // current page number

  statuses = [
    { key: 'NEW', value: 'New' },
    { key: 'INTERVIEWED', value: 'Interviewed' },
    { key: 'INVITED', value: 'Invited' },
    { key: 'CANCELLED', value: 'Cancelled' },];

  displayedColumns: string[] = ['title', 'candidateName', 'interviewers', 'schedule', 'result', 'status', 'job', 'action'];
  dataSource = new MatTableDataSource<any>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private interviewService: InterviewService,
    private userService: UserService,
    private router: Router,
    private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.getData();
  }

  getData(): void {
    this.interviewService.getInterviews(this.query).subscribe({
      next: data => {
        this.length = data.data.totalElements;
        this.pageSize = data.data.size;
        this.list = data.data.content;
        this.dataSource.data = this.list;
        this.getAllInterviewers();
      },
      error: err => {
        console.error('Error fetching interview data: ', err);
      }
    });
  }

  search() {
    this.getData();
  }

  clearSearch() {
    this.query = {
      keyword: null,
      page: 0,
      interviewerId: null,
      status: null,
    };
    this.paginator.pageIndex = 0;
    this.getData();
  }

  pageChangeEvent(event: PageEvent) {
    this.pageEvent = event;
    // this.length = event.length;
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.query.page = event.pageIndex;
    this.getData();
  }

  getAllInterviewers() {
    this.userService.getInterviewers().subscribe({
      next: (data: any) => {
        console.log('Interviewers data: ', data);
        this.interviewers = data.data;
      },
      error: err => {
        console.error('Error fetching interviewers data: ', err);
      }
    });
  }

  onAddNew() {
    this.router.navigate(['/interviews/add']);
  }

  formatInterviewers(interviewers: any[]): string {
    return interviewers.map(interviewer => interviewer.name).join(', ');
  }

  formatSchedule(element: any): string {
    return `${this.datePipe.transform(element.scheduleTime, 'dd-MM-yyyy')} from ${element.fromTime} to ${element.toTime}`;
  }

}
