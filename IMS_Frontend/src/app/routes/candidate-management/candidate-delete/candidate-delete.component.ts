import { Component, OnInit, Inject } from '@angular/core';
import { CandidateService } from '../candidate.service';
import { ToastrService } from 'ngx-toastr';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-candidate-delete',
  templateUrl: './candidate-delete.component.html',
  styleUrls: ['./candidate-delete.component.css']
})
export class CandidateDeleteComponent implements OnInit {
  candidateName: string = '';

  constructor(
    private candidateService: CandidateService,
    private toastr: ToastrService,
    public dialogRef: MatDialogRef<CandidateDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number }
  ) { }

  ngOnInit(): void {
    this.getCandidateDetails();
  }

  getCandidateDetails(): void {
    this.candidateService.getCandidateById(this.data.id).subscribe(
      (candidate) => {
        this.candidateName = candidate.fullName;
      },
      (error) => {
        this.toastr.error('Failed to load candidate details');
        this.dialogRef.close();
      }
    );
  }

  deleteCandidate(): void {
    this.candidateService.deleteCandidate(this.data.id).subscribe(
      () => {
        this.toastr.success('Candidate deleted successfully');
        this.dialogRef.close(true);  // Pass true to indicate success
      },
      (error) => {
        this.toastr.error('Failed to delete candidate');
      }
    );
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
