import { Component, OnInit } from '@angular/core';
import { OfferService } from '../offer.service';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-offer-details',
  templateUrl: './offer-details.component.html',
  styleUrls: ['./offer-details.component.css']
})

export class OfferDetailsComponent implements OnInit {

  offer!: OfferDetails;

  constructor(private offerService: OfferService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {

      this.getData(params['id']);
    });
  }

  getData(id: number) {
    this.offerService.getOfferById(id).subscribe({
      next: (data: any) => {
        console.log(data);
        this.offer = data.data;
      }
    });
  }

  cancelOffer() {
    this.offerService.cancelOffer(this.offer.id).subscribe({
      next: (data: any) => {
        const flag = data.data;
        if (flag) {
          this.toastr.success('Offer has been canceled successfully');
          location.reload();
        } else {
          this.toastr.error('Failed to cancel offer');
        }
      },
      error: (error: any) => {
        this.toastr.error('Failed to cancel offer');
      }
    });
  }

  approveOffer(flag: boolean) {
    this.offerService.approveOffer(this.offer.id, flag).subscribe({
      next: (data: any) => {
        if (flag) {
          this.toastr.success('Offer has been approved');
        } else {
          this.toastr.success('Offer has been rejected');
        }
        this.getData(this.offer.id);
      },
      error: (error: any) => {
        this.toastr.error('Failed to request');
      }
    });
  }

  acceptOffer(flag: boolean) {
    this.offerService.acceptOffer(this.offer.id, flag).subscribe({
      next: (data: any) => {
        if (flag) {
          this.toastr.success('Offer has been accepted');
        } else {
          this.toastr.success('Offer has been declined');
        }
        this.getData(this.offer.id);
      },
      error: (error: any) => {
        this.toastr.error('Failed to request');
      }
    });
  }

  markAsSentOffer() {
    this.offerService.markAsSentOffer(this.offer.id).subscribe({
      next: (data: any) => {
        this.toastr.success('Offer has been marked as sent');
        this.getData(this.offer.id);
      },
      error: (error: any) => {
        this.toastr.error('Failed to mark as sent');
      }
    });
  }

  get listInterviewers() {
    const interviewersInfo = this.offer.interview.interviewers.map((item: any) => {
      return item.fullName + ' (' + item.email + ')';
    });
    return interviewersInfo;
  }
}

export interface OfferDetails {
  id: number;
  candidate: {
    id: number,
    fullName: string,
    email: string,
  },

  contractType: string;
  contractTypeName: string;

  position: string;
  positionName: string;

  level: string;
  levelName: string;

  approver: {
    id: number,
    fullName: string,
    email: string,
  };

  department: string;
  departmentName: string;

  interviewInfo: string;
  interview: {
    id: number,
    title: string,
    note: string,
    interviewers: [],
  };
  recruiter: {
    id: number,
    fullName: string,
    email: string,
  };
  contractPeriodFrom: string;
  contractPeriodTo: string;
  dueDate: string;
  interviewNotes: string;
  basicSalary: string;
  note: string;

  status: string;
  statusName: string;

  createdAt: string;
  lastUpdatedBy: string;
};
