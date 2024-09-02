import { Component, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AdminLayoutComponent } from './components/admin-layout/admin-layout.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule, MAT_DATE_FORMATS, MAT_DATE_LOCALE, DateAdapter } from '@angular/material/core';
import { DashboardComponent } from './routes/dashboard/dashboard.component';
import { OfferDetailsComponent } from './routes/offer-management/offer-details/offer-details.component';
import { OfferFormComponent } from './routes/offer-management/offer-form/offer-form.component';
import { CandidateDetailsComponent } from './routes/candidate-management/candidate-details/candidate-details.component';
import { JobDetailsComponent } from './routes/job-management/job-details/job-details.component';
import { UserDetailsComponent } from './routes/user-management/user-details/user-details.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card'
import { MatSelectModule } from '@angular/material/select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OfferListComponent } from './routes/offer-management/offer-list/offer-list.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialogModule } from '@angular/material/dialog';
import { OfferExportDialogComponent } from './routes/offer-management/offer-export-dialog/offer-export-dialog.component';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MatMomentDateModule } from '@angular/material-moment-adapter'; // Import MomentDateAdapter
import { CURRENCY_MASK_CONFIG, CurrencyMaskConfig, CurrencyMaskModule } from 'ng2-currency-mask';
import { JobListComponent } from './routes/job-management/job-list/job-list.component';
import { JobFormComponent } from './routes/job-management/job-form/job-form.component';
import { CandidateListComponent } from './routes/candidate-management/candidate-list/candidate-list.component';
import { CandidateCreateComponent } from './routes/candidate-management/candidate-create/candidate-create.component';
import { CandidateEditComponent } from './routes/candidate-management/candidate-edit/candidate-edit.component';
import { CandidateDeleteComponent } from './routes/candidate-management/candidate-delete/candidate-delete.component';
import { CandidateBanComponent } from './routes/candidate-management/candidate-ban/candidate-ban.component';
import { InterviewAddComponent } from './routes/interview-management/interview-add/interview-add.component';
import { InterviewEditComponent } from './routes/interview-management/interview-edit/interview-edit.component';
import { InterviewScheduleComponent } from './routes/interview-management/interview-schedule/interview-schedule.component';
import { DatePipe } from '@angular/common';
import { InterviewDetailsComponent } from './routes/interview-management/interview-details/interview-details.component';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { LoginComponent } from './routes/session-management/login/login.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { JobDeleteComponent } from './routes/job-management/job-delete/job-delete.component';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { LoadingInterceptor } from './interceptors/loading.interceptor';
import { CandidateJobsComponent } from './routes/candidate-management/candidate-view-job/candidate-view-job.component';
import { InterviewSendComponent } from './routes/interview-management/interview-send/interview-send.component';

const MY_DATE_FORMAT = {
  parse: {
    dateInput: 'YYYY/MM/DD', // this is how your date will be parsed from Input
  },
  display: {
    dateInput: 'DD/MM/YYYY', // this is how your date will get displayed on the Input
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY'
  }
};

const CustomCurrencyMaskConfig: CurrencyMaskConfig = {
  align: "left",
  allowNegative: false,
  decimal: ",",
  precision: 2,
  prefix: "",
  suffix: " $",
  thousands: ".",
};

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    AdminLayoutComponent,
    NavbarComponent,
    DashboardComponent,
    OfferDetailsComponent,
    OfferFormComponent,
    CandidateDetailsComponent,
    JobDetailsComponent,
    UserDetailsComponent,
    OfferListComponent,
    OfferExportDialogComponent,
    JobListComponent,
    CandidateListComponent,
    JobFormComponent,
    CandidateCreateComponent,
    CandidateEditComponent,
    CandidateDeleteComponent,
    CandidateBanComponent,
    InterviewAddComponent,
    InterviewEditComponent,
    InterviewScheduleComponent,
    InterviewDetailsComponent,
    LoginComponent,
    JobDeleteComponent,
    InterviewDetailsComponent,
    LoginComponent,
    JobDeleteComponent,
    SpinnerComponent,
    CandidateJobsComponent,
    InterviewSendComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    CurrencyMaskModule,
    NgxMaterialTimepickerModule,
    // Material Theme
    MatMomentDateModule,
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatNativeDateModule,
    MatInputModule,
    MatSelectModule,
    MatDividerModule,
    MatGridListModule,
    MatDialogModule,
    MatDatepickerModule,
    MatAutocompleteModule,
    NgxMatSelectSearchModule,

    // Toastr
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right'
    }),
    NgbModule,
  ],
  providers: [
    // Interceptor
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true },

    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMAT },
    { provide: MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
    { provide: MAT_DATE_LOCALE, useValue: 'vi-VN' },
    { provide: CURRENCY_MASK_CONFIG, useValue: CustomCurrencyMaskConfig },
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

