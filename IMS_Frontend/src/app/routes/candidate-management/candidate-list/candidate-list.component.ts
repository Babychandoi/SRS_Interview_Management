import { Component, OnInit, ViewChild } from '@angular/core';
import { CandidateService } from '../candidate.service';
import { Candidate } from '../candidate.model';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CommonService } from 'src/app/services/common.service';
import { MatDialog } from '@angular/material/dialog';
import { CandidateDeleteComponent } from '../candidate-delete/candidate-delete.component';
import { CandidateJobsComponent } from '../candidate-view-job/candidate-view-job.component';


@Component({
  selector: 'app-candidate-list',
  templateUrl: './candidate-list.component.html',
  styleUrls: ['./candidate-list.component.css']
})
export class CandidateListComponent implements OnInit {

  query = {
    keyword: null,
    page: 0,
    status: null,
  };

  list: Candidate[] = [];
  pageEvent!: PageEvent;
  length = 0; // total number of offers
  pageSize = 0; // number of offers per page
  pageIndex = 0; // current page number

  statuses: any = [];
  displayedColumns: string[] = ["fullName", "email", "phoneNumber", "position", "recruiterName", "status", "action"];
  dataSource = new MatTableDataSource<Candidate>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private candiateService: CandidateService,
    private commonService: CommonService,
    private dialog: MatDialog
  ) { }

  async ngOnInit(): Promise<void> {
    this.getData();
    await this.getStatuses();
  }
  openDeleteDialog(candidateId: number): void {
    const dialogRef = this.dialog.open(CandidateDeleteComponent, {
      width: '400px',
      data: { id: candidateId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.list = this.list.filter(candidate => candidate.id !== candidateId);
        this.dataSource.data = this.list; }
     });
  }
  openJobsDialog(candidateId: number,  candidateName: string): void {
    const dialogRef = this.dialog.open(CandidateJobsComponent, {
      width: '800px',
      data: { id: candidateId, name : candidateName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Xử lý sau khi hộp thoại đóng, nếu cần
      }
    });
  }

  

  getData() {
    console.log(this.query);
    this.candiateService.getAllCandidates(this.query).subscribe({
      next: data => {
        console.log(data);
        this.length = data.data.totalElements;
        this.pageSize = data.data.size;
        this.list = data.data.content;
        this.dataSource.data = this.list;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  async getStatuses() {
    this.commonService.getCandidateStatuses().subscribe({
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
    });
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
    this.paginator.pageIndex = 0;
    this.getData();
  }
}

