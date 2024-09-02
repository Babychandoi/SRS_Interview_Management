import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { JobService } from '../job.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CommonService } from 'src/app/services/common.service';
import { MatDialog } from '@angular/material/dialog';
import { JobDeleteComponent } from '../job-delete/job-delete.component';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css']
})
export class JobListComponent implements OnInit {

  query = {
    keyword: null,
    page: 0,
    status: null,
  };

  list: Job[] = [];
  pageEvent!: PageEvent;
  length = 0; // total number of offers
  pageSize = 0; // number of offers per page
  pageIndex = 0; // current page number

  statuses: any = [];

  displayedColumns: string[] = ["title", "skills", "startDate", "endDate", "level", "status", "action"];
  dataSource = new MatTableDataSource<Job>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private jobService: JobService,
    private commonService: CommonService,
    public dialog: MatDialog,
    public cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    this.getData();
    this.cdr.detectChanges();
    await this.getStatuses();
  }

  getData() {
    this.jobService.getJobs(this.query).subscribe({
      next: data => {
        this.length = data.data.totalElements;
        this.pageSize = data.data.size;
        this.list = data.data.content;
        this.dataSource.data = this.list;
      },
      error: error => {
        console.error(error);
      }
    });
  }

  async getStatuses() {
    this.commonService.getJobStatuses().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.statuses = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key],
          }));
        }
      },
      error: err => {
        console.log(err);
        // this.toastr.error(err);
      }
    })
  }

  pageChangeEvent(event: PageEvent) {
    this.pageEvent = event;
    // this.length = event.length;
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.query.page = event.pageIndex;
    this.getData();
  }

  search() {
    this.getData();
  }

  clearSearch() {
    this.query = {
      keyword: null,
      page: 0,
      status: null,
    };
    this.getData();
  }

  handleDelete(id: number): void {
    this.dialog.open(JobDeleteComponent, {
      width: '250px',
      data: { id: id }
    });
  }
}

interface Job {
  id: number;
  title: string;
  skills: string[];
  startDate: Date;
  endDate: Date;
  levels: string;
  status: string;
  createdDate: Date;
}
