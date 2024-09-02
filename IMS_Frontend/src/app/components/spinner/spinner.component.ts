import { Component } from '@angular/core';
import { BusyService } from 'src/app/services/busy.service';

@Component({
  selector: 'app-spinner',
  template: `
    <div class="spinner-overlay" *ngIf="isLoading$ | async">
      <mat-progress-spinner
        class="spinner"
        [diameter]="50"
        mode="indeterminate">
      </mat-progress-spinner>
    </div>
  `,
  styles: [`
    .spinner-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(255, 255, 255, 0.8);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 1000;
    }
  `]
})
export class SpinnerComponent {
  isLoading$ = this.busyService.isLoading$;

  constructor(private busyService: BusyService) { }
}
