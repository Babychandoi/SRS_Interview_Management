import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { OfferService } from '../offer.service';
import { ToastrService } from 'ngx-toastr';
import { MatPaginator, MatPaginatorIntl, PageEvent } from '@angular/material/paginator';
import { CommonService } from 'src/app/services/common.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { OfferExportDialogComponent } from '../offer-export-dialog/offer-export-dialog.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-offer-list',
  templateUrl: './offer-list.component.html',
  styleUrls: ['./offer-list.component.css']
})
export class OfferListComponent implements OnInit {

  query = {
    keyword: null,
    page: 0,
    department: null,
    status: null,
  };

  list: Offer[] = [];

  pageEvent!: PageEvent;
  length = 0; // total number of offers
  pageSize = 0; // number of offers per page
  pageIndex = 0; // current page number


  departments: any = [];
  statuses: any = [];

  displayedColumns: string[] = ["candidateName", "email", "approver", "department", "notes", "status", "action"];
  dataSource = new MatTableDataSource<Offer>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;


  constructor(private offerService: OfferService,
    private commonService: CommonService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {
  }

  async ngOnInit(): Promise<void> {
    this.getData();
    await this.getDepartments();
    await this.getStatuses();
  }

  getData() {
    this.offerService.getOffers(this.query).subscribe({
      next: data => {
        this.length = data.data.totalElements;
        this.pageSize = data.data.size;
        this.list = data.data.content;
        this.dataSource.data = this.list;
      },
      error: err => {
        console.log(err);
        // this.toastr.error(err);
      }
    });
  }

  async getDepartments() {
    this.commonService.getDepartments().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.departments = Object.keys(data.data).map(key => ({
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

  async getStatuses() {
    this.commonService.getOfferStatuses().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.statuses = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key]
          }))
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
      department: null,
      status: null,
    };
    this.paginator.pageIndex = 0;
    this.getData();
  }

  export() {
    const dialogRef = this.dialog.open(OfferExportDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Export submitted');
      }
    });
  }
}

export interface Offer {
  id: number,
  candidateName: string,
  email: string,
  approver: string,
  department: string,
  note: string,
  status: string
};

