import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CandidateService } from '../candidate.service';
import { ToastrService } from 'ngx-toastr';
import { Candidate } from '../candidate.model';
import { MatDialog } from '@angular/material/dialog';
import { CandidateBanComponent } from '../candidate-ban/candidate-ban.component';

@Component({
  selector: 'app-candidate-details',
  templateUrl: './candidate-details.component.html',
  styleUrls: ['./candidate-details.component.css']
})
export class CandidateDetailsComponent implements OnInit {
  candidate: Candidate | undefined;

  constructor(
    private route: ActivatedRoute,
    private candidateService: CandidateService,
    private toastr: ToastrService,
    private router: Router,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void{
    this.loadCandiate();
  }
  loadCandiate(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.candidateService.getCandidateById(+id).subscribe(
        (candidate) => {
          this.candidate = candidate;

          const fileBaseUrl = 'http://localhost:8080/api/files/';
          if (this.candidate && this.candidate.cvUrl) {
            console.log('Updated CV URL:', this.candidate.cvUrl); // Kiểm tra URL mới
            this.candidate.cvUrl = fileBaseUrl + encodeURIComponent(this.candidate.cvUrl.trim());
            console.log('Updated CV URL:', this.candidate.cvUrl.replace("uploads%2F", "")); // Kiểm tra URL mới
            this.candidate.cvUrl = this.candidate.cvUrl.replace("uploads%2F", "");
          }
          console.log(candidate)
          console.log(candidate.job)
        },
        (error) => {
          this.toastr.error('Failed to load candidate details');
          this.router.navigate(['/candidates']);
        }
      );
    } else {
      this.toastr.error('Invalid candidate ID');
      this.router.navigate(['/candidates']);
    }
  }

  openBanDialog(): void {
    if (this.candidate) {
      const dialogRef = this.dialog.open(CandidateBanComponent, {
        width: '400px',
        data: { id: this.candidate.id, name: this.candidate.fullName }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.loadCandiate(); // Tải lại thông tin ứng viên
        }
      });
    }
  }

  editCandidate(): void {
    if (this.candidate && this.candidate.id) {
      this.router.navigate(['/candidates/edit', this.candidate.id]);
    }
  }

  cancel(): void {
    this.router.navigate(['/candidates']);
  }

}
