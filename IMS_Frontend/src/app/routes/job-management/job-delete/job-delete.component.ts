import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

import { ToastrService } from 'ngx-toastr';
import { JobService } from '../job.service';

@Component({
  selector: 'app-job-delete',
  templateUrl: './job-delete.component.html',
  styleUrls: ['./job-delete.component.css'],
})
export class JobDeleteComponent {
  constructor(public dialogRef: MatDialogRef<JobDeleteComponent>,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
    private jobService: JobService,
    @Inject(MAT_DIALOG_DATA) public data: { id: number },

  ) { }

  onConfirm(): void {
    this.jobService.deleteJob(this.data.id).subscribe({
      next: () => {
        this.toastr.success('Job deleted successfully!');
        this.cdr.detectChanges();
        window.location.reload();
        console.log('Job deleted successfully!')
      },
      error: (err) => {
        if (err.status === 404) {
          this.toastr.warning('Job didn\'t exist!');
        } else {
          this.toastr.error('Failed to delete job!');
        }
      }
    });
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
