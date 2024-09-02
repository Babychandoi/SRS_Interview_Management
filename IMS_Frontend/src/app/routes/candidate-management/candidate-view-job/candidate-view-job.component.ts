import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CandidateService } from '../candidate.service';
import { ToastrService } from 'ngx-toastr';
import { JobDetailsDTO } from '../candidate.model'; // Import DTO
import { Router } from '@angular/router';

@Component({
  selector: 'app-candidate-jobs',
  templateUrl: './candidate-view-job.component.html',
  styleUrls: ['./candidate-view-job.component.css']
})
export class CandidateJobsComponent implements OnInit {
  jobs: JobDetailsDTO[] = [];
  displayedColumns: string[] = ['title', 'workingAddress', 'description', 'actions'];
  candidateId: number;
  candidateName: string;

  constructor(
    private candidateService: CandidateService,
    public dialogRef: MatDialogRef<CandidateJobsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number, name: string },
    private router: Router
  ) {
    this.candidateId = data.id;
    this.candidateName = data.name;
  }

  ngOnInit(): void {
    this.getJobs();
  }

  getJobs(): void {
    this.candidateService.getJobsByCandidateId(this.candidateId).subscribe(
      (jobs) => {
        this.jobs = jobs;
      },
      (error) => {
        console.error('Failed to load jobs', error);
      }
    );
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
  navigateToInterviewAdd(jobId: number): void {
    this.router.navigate(['/interviews/create'], {
      queryParams: {
        candidateName: this.candidateName,
        candidateId: this.candidateId,
        jobId: jobId
      }
    });
    this.dialogRef.close();
  }
}
