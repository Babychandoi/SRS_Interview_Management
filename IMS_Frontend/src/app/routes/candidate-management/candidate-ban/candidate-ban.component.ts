import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CandidateService } from '../candidate.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-candidate-ban',
  templateUrl: './candidate-ban.component.html',
  styleUrls: ['./candidate-ban.component.css']
})
export class CandidateBanComponent implements OnInit {
  candidateId: number = 0;
  candidateName: string = '';

  constructor(
    private candidateService: CandidateService,
    private toastr: ToastrService,
    public dialogRef: MatDialogRef<CandidateBanComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number, name: string }
  ) { }

  ngOnInit(): void {
    this.candidateId = this.data.id;
    this.candidateName = this.data.name;
  }

  banCandidate(): void {
    this.candidateService.banCandidate(this.candidateId).subscribe(
      () => {
        this.toastr.success('Candidate banned successfully');
        this.dialogRef.close(true); // Đóng hộp thoại và gửi kết quả là true
      },
      (error) => {
        this.toastr.error('Failed to ban candidate');
      }
    );
  }

  cancel(): void {
    this.dialogRef.close(); // Đóng hộp thoại mà không gửi kết quả
  }
}
