import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InterviewService } from '../interview.service';
import { DatePipe } from '@angular/common';
import { Location } from '@angular/common';

interface Job {
  id: number;
  position: string[];
  recruiter: string;
}

interface Position {
  idJob: number;
  title: string;
}

interface Candidate {
  id: number;
  name: string;
  email: string;
  positions: Position[];
}

interface Interviewer {
  id: number;
  name: string;
  email: string;
}

interface Interview {
  id: number;
  title: string;
  candidateName: string;
  scheduleTime: string;
  fromTime: string;
  toTime: string;
  note: string;
  job: Job[];
  interviewers: Interviewer[];
  location: string;
  recruiter: string;
  meetingId: string;
  result: string;
  status: string;
  jobTitle: string;
  positionLevel: string;
  candidate: Candidate[];
  userinterviewers: Interviewer[];
}

@Component({
  selector: 'app-interview-edit',
  templateUrl: './interview-edit.component.html',
  styleUrls: ['./interview-edit.component.css'],
})
export class InterviewEditComponent implements OnInit {
  interview: Interview = {
    id: 0,
    title: '',
    candidateName: '',
    scheduleTime: '',
    fromTime: '',
    toTime: '',
    note: '',
    job: [],
    interviewers: [],
    location: '',
    recruiter: '',
    meetingId: '',
    result: '',
    status: '',
    jobTitle: '',
    positionLevel: '',
    candidate: [],
    userinterviewers: []
  };
  errorMessage: string | null = null;
  selectedCandidateId: number | null = null;
  selectedJobPosition: string | null = null;
  jobPositions: string[] = [];
  filteredJobTitles: string[] = []; // Filtered job titles based on candidate positions
  candidates: Candidate[] = [];
  jobs: Job[] = [];
  today: Date = new Date();
  scheduleDateError: boolean = false;
  timeError: boolean = false;
  PoLevel: string = '';
  constructor(
    private route: ActivatedRoute,
    private interviewService: InterviewService,
    private router: Router,
    private datePipe: DatePipe,
    private location: Location
  ) { }

  ngOnInit(): void {
    const interviewId = Number(this.route.snapshot.paramMap.get('id'));
    this.interviewService.getUpdateInterview(interviewId).subscribe(
      (data: any) => {
        if (data) {
          this.interview = data as Interview;
          this.candidates = data.candidate;
          this.jobs = data.job;
          this.filteredJobTitles = [];
          const formattedDate = this.datePipe.transform(new Date(this.interview.scheduleTime), 'yyyy-MM-dd');
          if (formattedDate) {
            this.interview.scheduleTime = formattedDate;
          }

          this.PoLevel = this.interview.positionLevel;
          this.selectedJobPosition = this.interview.jobTitle;
          this.onCandidateChange(this.interview.candidateName);
          this.updatePositionLevels();
          this.updateJobData();
          console.log('Interview data:', this.PoLevel);
          this.updateJobData();
          if (Array.isArray(this.interview.userinterviewers) && Array.isArray(this.interview.interviewers)) {
            this.interview.interviewers = this.interview.userinterviewers.filter(interviewer =>
              this.interview.interviewers.some(selectedInterviewer => selectedInterviewer.id === interviewer.id)
            );
          }
        } else {
          this.errorMessage = 'No data found';
          console.error('No interview data received');
        }
      },
      error => {
        this.errorMessage = 'Error loading interview data';
        console.error('Error loading interview data', error);
      }
    );
  }
  onCandidateChange(selectedName: string) {
    const selectedCandidate = this.candidates.find(candidate => candidate.name === selectedName);
    if (selectedCandidate) {
      this.selectedCandidateId = selectedCandidate.id;
      this.filteredJobTitles = selectedCandidate.positions.map(position => position.title);
    } else {
      this.selectedCandidateId = null;
      this.filteredJobTitles = [];
    }
  }

  updatePositionLevels() {
    console.log('Updating Position Levels...');
    console.log('Selected Job Position:', this.selectedJobPosition);

    const selectedCandidate = this.candidates.find(candidate =>
      candidate.positions.some(position => position.title === this.selectedJobPosition)
    );

    if (!selectedCandidate) {
      console.log('No matching candidate found.');
      this.jobPositions = [];
      this.interview.recruiter = '';
      return;
    }

    const selectedJob = this.jobs.find(job =>
      job.id === selectedCandidate.positions.find(position => position.title === this.selectedJobPosition)?.idJob
    );

    if (selectedJob) {
      console.log('Selected Job:', selectedJob);
      this.jobPositions = selectedJob.position;
      this.interview.recruiter = selectedJob.recruiter;

      // Đảm bảo PoLevel có trong jobPositions
      if (this.jobPositions.includes(this.PoLevel)) {
        console.log('PoLevel is valid:', this.PoLevel);
      } else {
        console.log('PoLevel is not in jobPositions:', this.PoLevel);
      }
    } else {
      console.log('No matching job found.');
      this.jobPositions = [];
      this.interview.recruiter = '';
    }

    console.log('Updated Job Positions:', this.jobPositions);
  }


  updateJobData() {
    console.log('Selected Job Position:', this.selectedJobPosition);
    console.log('Available Jobs:', this.jobs);

    // Tìm job phù hợp với title trong positions của candidate
    this.interview.job = this.jobs
      .filter((job: Job) => {
        return this.candidates
          .some(candidate =>
            candidate.positions
              .some(position =>
                position.title === this.selectedJobPosition && position.idJob === job.id
              )
          );
      })
      .map((job: Job) => ({
        ...job,
        position: job.position.filter(position => position === this.selectedJobPosition)
      }));

    console.log('Filtered Job Data:', this.interview.job);
  }

  onSubmit() {
    if (this.isFormInvalid()) {
      return; // Prevent submission if form is invalid
    }
    this.validateTimes(); // Ensure times are valid before submission

    if (this.scheduleDateError || this.timeError) {
      return; // Prevent submission if errors are present
    }

    if (this.interview) {
      this.updateJobData();
      this.interview.job[0].position.push(this.PoLevel || '');
      // Update candidate list based on selected name
      this.interview.candidate = this.candidates.filter(candidate => candidate.name === this.interview.candidateName);


      this.interview.scheduleTime = this.datePipe.transform(this.interview.scheduleTime, 'yyyy-MM-dd') || '';
      console.log('Interview to be added:', this.interview);
      this.interviewService.updateInterview(this.interview).subscribe(
        response => {
          this.router.navigate(['/interviews']);
        },
        error => {
          this.errorMessage = 'Error updating interview';
          console.error('Error updating interview', error);
        }
      );
    }
  }

  onCancel() {
    this.location.back();
  }

  updateRecruiter() {
    const selectedJob = this.interview.job.find(job =>
      job.position.includes(this.selectedJobPosition || '')
    );
    if (selectedJob) {
      this.interview.recruiter = selectedJob.recruiter;
    } else {
      this.interview.recruiter = '';
    }
  }

  validateScheduleDate(selectedDate: Date) {
    this.scheduleDateError = new Date(selectedDate) <= new Date();
  }

  validateTimes() {
    const startTime = new Date(`1970-01-01T${this.interview.fromTime}:00`);
    const endTime = new Date(`1970-01-01T${this.interview.toTime}:00`);
    this.timeError = endTime <= startTime;
  }
  isFormInvalid(): boolean {
    this.errorMessage = null;
    if (!this.interview.candidateName) {
      this.errorMessage = 'Candidate name is required';
      return true;
    }
    if (!this.selectedJobPosition) {
      this.errorMessage = 'Job position is required';
      return true;
    }
    if (!this.interview.fromTime) {
      this.errorMessage = 'Start time is required';
      return true;
    }
    if (!this.interview.toTime) {
      this.errorMessage = 'End time is required';
      return true;
    }
    if (this.timeError) {
      this.errorMessage = 'End time must be greater than start time';
      return true;
    }
    if (this.scheduleDateError) {
      this.errorMessage = 'Schedule date must be greater than today';
      return true;
    }

    return false;
  }
}
