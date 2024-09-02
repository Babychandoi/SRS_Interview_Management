import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
} from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { JobService } from '../job.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService } from 'src/app/services/common.service';
import { benefitOptions, levelOptions, skillOptions } from '../options';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatFormField } from '@angular/material/form-field';

@Component({
  selector: 'app-job-form',
  templateUrl: './job-form.component.html',
  styleUrls: ['./job-form.component.css'],
})
export class JobFormComponent implements OnInit {
  jobForm: FormGroup;
  jobId: number | null = null;
  job: any;
  skillOptions: any;
  levelOptions: any;
  benefitOptions: any;
  endDateDp: any;
  startDateDp: any;


  constructor(
    private fb: FormBuilder,
    private jobService: JobService,
    private commonService: CommonService,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router
  ) {
    this.jobForm = this.fb.group({
      id: [null],
      title: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      salaryStartRange: ['', Validators.required],
      salaryEndRange: ['', Validators.required],
      workingAddress: ['', Validators.required],
      skills: [[], Validators.required], // Sử dụng mảng cho skills
      levels: [[], Validators.required],
      benefits: [[], Validators.required],
      description: [''],
      status: [null],
    });
  }

  ngOnInit(): void {
    this.initializeFormArrays();

    this.route.paramMap.subscribe((params) => {
      const jobId = params.get('id');
      if (jobId) {
        this.jobId = +jobId;
        this.jobService.getJobById(jobId).subscribe({
          next: (data) => {
            if (data.data) {
              this.job = data.data;
              this.jobForm.patchValue({
                id: data.data.id,
                title: data.data.title,
                startDate: data.data.startDate,
                endDate: data.data.endDate,
                salaryStartRange: data.data.salaryStartRange,
                salaryEndRange: data.data.salaryEndRange,
                workingAddress: data.data.workingAddress,
                skills: data.data.skills,
                levels: data.data.levels,
                benefits: data.data.benefits,
                description: data.data.description,
                status: data.data.status,
              });
            }
          },
          error: (error) => {
            console.log(error);
          },
        });
      }
    });
  }

  initializeFormArrays(): void {
    this.skillOptions = skillOptions;
    this.benefitOptions = benefitOptions;
    this.levelOptions = levelOptions;
  }

  onSubmit(): void {
    if (
      this.jobForm.get(['id'])!.value !== null &&
      this.jobForm.get(['id'])!.value !== undefined
    ) {
      this.updateJob();
    } else {
      this.createNewJob();
    }
  }

  createNewJob(): void {
    this.jobService.createJob(this.jobForm.value).subscribe({
      next: () => {
        this.toastr.success('Successfully created job');
        this.router.navigate(['/jobs']);
      },
      error: () => {
        this.toastr.error('Failed to create job');
      },
    });
  }

  updateJob(): void {
    this.jobService.updateJob(this.jobForm.value).subscribe({
      next: () => {
        this.toastr.success('Successfully updated job');
        this.router.navigate(['/jobs']);
      },
      error: () => {
        this.toastr.error('Failed to update job');
      },
    });
  }
  cancel(): void {
    this.router.navigate(['/jobs']);
  }


}
