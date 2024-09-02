import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { OfferService } from '../offer.service';
import { CommonService } from 'src/app/services/common.service';
import { ToastrService } from 'ngx-toastr';
import { Form, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { last, min, ReplaySubject, Subject, Subscription, take, takeUntil } from 'rxjs';
import { CandidateService } from '../../candidate-management/candidate.service';
import { InterviewService } from '../../interview-management/interview.service';
import { MatSelect } from '@angular/material/select';
import { UserService } from '../../user-management/user.service';

@Component({
  selector: 'app-offer-form',
  templateUrl: './offer-form.component.html',
  styleUrls: ['./offer-form.component.css']
})
export class OfferFormComponent implements OnInit, OnDestroy {

  offerForm!: FormGroup;
  offerId: number | null = null;
  formChangesSubscription: Subscription | null = null;
  isFormChanged = false;

  constructor(private offerService: OfferService,
    private userService: UserService,
    private commonService: CommonService,
    private candidateService: CandidateService,
    private interviewService: InterviewService,
    private toastr: ToastrService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {

  }

  // Enums
  positions: any;
  contractTypes: any;
  levels: any;
  departments: any;

  // Entities
  candidates: any = [];
  candidateCtrl: FormControl = new FormControl();
  candidateFilterCtrl: FormControl = new FormControl();
  filteredCandidates: ReplaySubject<any[]> = new ReplaySubject<any[]>(1);
  @ViewChild('singleSelect') singleSelect!: MatSelect;

  interviewInfos: any = [];
  selectedInterview: any = { note: 'N/A' };

  recruiterOwners: any = [];
  recruiterFilterCtrl: FormControl = new FormControl();
  filteredRecruiters: ReplaySubject<any[]> = new ReplaySubject<any[]>(1);

  approvers: any = [];
  approverFilterCtrl: FormControl = new FormControl();
  filteredApprovers: ReplaySubject<any[]> = new ReplaySubject<any[]>(1);

  offer!: Offer;

  minDate: Date = new Date();

  private _onDestroy = new Subject<void>();

  ngOnInit(): void {
    this.initForm();
    this.getCandidates();
    this.getApproversAndRecruiters();
    this.getAllEnums();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.offerId = +id;
        this.getOfferDetails(this.offerId);
      }
    });

    // Track form to check if there are any changes 
    // to notify user before leaving the page
    this.trackFormChanges();

    // Subscribe to candidate changes to get interviews
    this.offerForm.get('candidate')?.valueChanges.subscribe((candidateId) => {
      if (candidateId) {
        this.getInterviewsByCandidateId(candidateId);
      } else {
        this.interviewInfos = [];
      }
    });

    this.candidateFilterCtrl.valueChanges.pipe(takeUntil(this._onDestroy)).subscribe(() => {
      this.filterCandidates();
    });

    this.approverFilterCtrl.valueChanges.pipe(takeUntil(this._onDestroy)).subscribe(() => {
      this.filterApprovers();
    });

    this.recruiterFilterCtrl.valueChanges.pipe(takeUntil(this._onDestroy)).subscribe(() => {
      this.filterRecruiters();
    });

    // Subscribe to interview changes to get selected interview note
    this.offerForm.get('interview')?.valueChanges.subscribe((interviewId) => {
      if (interviewId) {
        const interview = this.interviewInfos.find((interview: { id: any; }) => interview.id === interviewId);
        this.selectedInterview = interview ? { ...interview, note: interview.note || 'N/A' } : { note: 'N/A' };
      } else {
        this.selectedInterview = null;
      }
    });
  }


  initForm() {
    this.offerForm = this.fb.group({
      candidate: [null, Validators.required],
      position: ['', Validators.required],
      approver: [null],
      interview: [null, Validators.required],
      contractPeriodFrom: [null, Validators.required],
      contractPeriodTo: [null, Validators.required],
      contractType: ['', Validators.required],
      level: [null, Validators.required],
      department: ['', Validators.required],
      recruiter: [null],
      dueDate: ['', Validators.required],
      basicSalary: ['', [Validators.required, Validators.min(0)]],
      note: [''],
      status: [null],
      createdAt: [''],
      lastUpdateBy: [null]
    });
  }

  getCandidates() {
    this.candidateService.getAllNotBannedCandidates().subscribe({
      next: (data: any) => {
        this.candidates = data.data;
        this.filteredCandidates.next(this.candidates.slice());
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  getApproversAndRecruiters() {
    this.userService.getAllUsers().subscribe({
      next: (data: any) => {
        // this.approvers = data.data.filter((user: { role: string; }) => user.role === 'APPROVER');
        // this.recruiterOwners = data.data.filter((user: { role: string; }) => user.role === 'RECRUITER');
        this.approvers = data.data;
        this.filteredApprovers.next(this.approvers.slice());
        this.recruiterOwners = data.data;
        this.filteredRecruiters.next(this.recruiterOwners.slice());
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  getInterviewsByCandidateId(candidateId: any) {
    this.interviewService.getInterviewsByCandidateId(candidateId).subscribe({
      next: (data: any) => {
        this.interviewInfos = data.data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getAllEnums() {
    this.commonService.getPositions().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.positions = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key],
          }));
        }
      }
    })

    this.commonService.getContractTypes().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.contractTypes = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key],
          }));
        }
      }
    })

    this.commonService.getPositionLevels().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.levels = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key],
          }));
        }
      }
    })

    this.commonService.getDepartments().subscribe({
      next: (data: any) => {
        if (data.data) {
          this.departments = Object.keys(data.data).map(key => ({
            key: key,
            value: (data.data as any)[key],
          }));
        }
      }
    })
  }

  saveOffer() {
    if (this.offerForm.invalid) {
      return;
    }
    const offerData = this.offerForm.value;
    offerData.candidate = { id: offerData.candidate };
    offerData.interview = { id: offerData.interview };
    offerData.approver = { id: offerData.approver };
    offerData.recruiter = { id: offerData.recruiter };
    console.log(offerData);
    if (this.offerId) {
      offerData.id = this.offerId;
      this.offerService.updateOffer(offerData).subscribe({
        next: (data) => {
          this.toastr.success('Offer updated successfully');
          this.router.navigate(['/offers']);
        },
        error: _ => {
          this.toastr.error('Failed to update offer');
        }
      })
    } else {
      this.offerService.addOffer(offerData).subscribe({
        next: (data) => {
          this.toastr.success('Offer added successfully');
          this.router.navigate(['/offers']);
        },
        error: _ => {
          this.toastr.error('Failed to add offer');
        }
      })
    }

  }

  getOfferDetails(id: number) {
    this.offerService.getOfferById(id).subscribe({
      next: (data) => {
        const offer = data.data;
        this.offerForm.patchValue(offer);
        if (offer.candidate) {
          this.offerForm.get('candidate')?.setValue(offer.candidate.id);
        }
        if (offer.interview) {
          this.offerForm.get('interview')?.setValue(offer.interview.id);
        }
        if (offer.approver) {
          this.offerForm.get('approver')?.setValue(offer.approver.id);
        }
        if (offer.recruiter) {
          this.offerForm.get('recruiter')?.setValue(offer.recruiter.id);
        }
        minDate: new Date(offer.contractPeriodFrom);
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  trackFormChanges() {
    this.formChangesSubscription = this.offerForm.valueChanges.subscribe(() => {
      this.isFormChanged = true;
    });
  }

  cancel() {
    if (this.isFormChanged) {
      const confirmLeave = confirm('Are you sure you want to leave this page? Unsaved changes will be lost.');
      if (!confirmLeave) {
        return;
      }
    }
    this.router.navigate(['/offers']);
  }

  filterCandidates() {
    if (!this.candidates) {
      return;
    }

    let search = this.candidateFilterCtrl.value;
    if (!search) {
      this.filteredCandidates.next(this.candidates.slice());
      return;
    } else {
      search = search.toLowerCase();
    }

    this.filteredCandidates.next(
      this.candidates.filter((candidate: { fullName: string; email: string }) =>
        candidate.fullName.toLowerCase().indexOf(search) > -1 || candidate.email.toLowerCase().indexOf(search) > -1
      ));
  }

  filterApprovers() {
    if (!this.approvers) {
      return;
    }

    let search = this.approverFilterCtrl.value;
    if (!search) {
      this.filteredApprovers.next(this.approvers.slice());
      return;
    } else {
      search = search.toLowerCase();
    }

    this.filteredApprovers.next(
      this.approvers.filter((approver: { fullName: string; email: string }) =>
        approver.fullName.toLowerCase().indexOf(search) > -1 || approver.email.toLowerCase().indexOf(search) > -1
      ));
  }

  filterRecruiters() {
    if (!this.recruiterOwners) {
      return;
    }

    let search = this.recruiterFilterCtrl.value;
    if (!search) {
      this.filteredRecruiters.next(this.recruiterOwners.slice());
      return;
    } else {
      search = search.toLowerCase();
    }

    this.filteredRecruiters.next(
      this.recruiterOwners.filter((recruiter: { fullName: string; email: string }) =>
        recruiter.fullName.toLowerCase().indexOf(search) > -1 || recruiter.email.toLowerCase().indexOf(search) > -1
      ));
  }

  ngOnDestroy() {
    if (this.formChangesSubscription) {
      this.formChangesSubscription.unsubscribe();
    }
  }
}

export interface Offer {
  id: number;
  candidate: {
    id: number
  }
  position: string, // enum
  approver: {
    id: number
  },
  interview: {
    id: number
  }
  contractPeriodFrom: Date,
  contractPeriodTo: Date,
  contractType: string, // enum
  level: string,  // enum
  status: string, // enum
  department: string, // enum
  recruiter: {
    id: number
  },
  dueDate: Date,
  basicSalary: number,
  note: string,
  createdAt: Date,
  lastUpdateBy: {
    id: number
  }
}