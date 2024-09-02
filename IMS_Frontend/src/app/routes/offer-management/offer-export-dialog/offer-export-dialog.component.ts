import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { OfferService } from '../offer.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-offer-export-dialog',
  templateUrl: './offer-export-dialog.component.html',
  styleUrls: ['./offer-export-dialog.component.css']
})
export class OfferExportDialogComponent {

  exportForm!: FormGroup;

  constructor(public dialogRef: MatDialogRef<OfferExportDialogComponent>,
    private offerService: OfferService,
    private fb: FormBuilder,
    private datePipe: DatePipe) {
    this.exportForm = this.fb.group({
      from: [null, Validators.required],
      to: [null, Validators.required],
    });
  }

  onSubmit() {
    const exportData = {
      from: this.exportForm.get('from')?.value,
      to: this.exportForm.get('to')?.value,
    };
    const from = this.datePipe.transform(exportData.from, 'dd-MM-yyyy');
    const to = this.datePipe.transform(exportData.to, 'dd-MM-yyyy');
    this.offerService.exportOffers(exportData).subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `OfferList-${from}_${to}.xls`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    });
  }
}
