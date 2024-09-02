import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './routes/dashboard/dashboard.component';
import { AdminLayoutComponent } from './components/admin-layout/admin-layout.component';
import { OfferDetailsComponent } from './routes/offer-management/offer-details/offer-details.component';
import { InterviewDetailsComponent } from './routes/interview-management/interview-details/interview-details.component';
import { JobDetailsComponent } from './routes/job-management/job-details/job-details.component';
import { CandidateDetailsComponent } from './routes/candidate-management/candidate-details/candidate-details.component';
import { UserDetailsComponent } from './routes/user-management/user-details/user-details.component';
import { OfferListComponent } from './routes/offer-management/offer-list/offer-list.component';
import { OfferFormComponent } from './routes/offer-management/offer-form/offer-form.component';
import { CandidateListComponent } from './routes/candidate-management/candidate-list/candidate-list.component';
import { JobListComponent } from './routes/job-management/job-list/job-list.component';
import { JobFormComponent } from './routes/job-management/job-form/job-form.component';
import { CandidateCreateComponent } from './routes/candidate-management/candidate-create/candidate-create.component';
import { CandidateDeleteComponent } from './routes/candidate-management/candidate-delete/candidate-delete.component';
import { CandidateBanComponent } from './routes/candidate-management/candidate-ban/candidate-ban.component';
import { CandidateEditComponent } from './routes/candidate-management/candidate-edit/candidate-edit.component';
import { InterviewAddComponent } from './routes/interview-management/interview-add/interview-add.component';
import { InterviewScheduleComponent } from './routes/interview-management/interview-schedule/interview-schedule.component';
import { InterviewEditComponent } from './routes/interview-management/interview-edit/interview-edit.component';
import { LoginComponent } from './routes/session-management/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { GuestGuard } from './guards/guest.guard';
import { InterviewSendComponent } from './routes/interview-management/interview-send/interview-send.component';
const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },

      // Offers
      { path: 'offers', component: OfferListComponent },
      { path: 'offers/create', component: OfferFormComponent },
      { path: 'offers/edit/:id', component: OfferFormComponent },
      { path: 'offers/:id', component: OfferDetailsComponent },

      // Interviews
      { path: 'interviews', component: InterviewScheduleComponent },
      { path: 'interviews/create', component: InterviewAddComponent },
      { path: 'interviews/edit/:id', component: InterviewEditComponent },
      { path: 'interviews/:id', component: InterviewDetailsComponent },
      {path: 'interviews/send/:id', component: InterviewSendComponent},
      // Jobs
      { path: 'jobs', component: JobListComponent },
      { path: 'jobs/create', component: JobFormComponent },
      { path: 'jobs/edit/:id', component: JobFormComponent },
      { path: 'jobs/:id', component: JobDetailsComponent },

      // Candidates
      { path: 'candidates', component: CandidateListComponent },
      { path: 'candidates/create', component: CandidateCreateComponent },
      { path: 'candidates/:id', component: CandidateDetailsComponent },
      { path: 'candidates/edit/:id', component: CandidateEditComponent },
      { path: 'candidates/delete/:id', component: CandidateDeleteComponent },
      { path: 'candidates/ban/:id', component: CandidateBanComponent },


      // Users
      { path: 'users', component: UserDetailsComponent },
    ]
  },
  {
    path: 'login',
    canActivate: [GuestGuard],
    component: LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
