import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InterviewService } from '../interview.service';
import { DatePipe } from '@angular/common';

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

interface Job {
  id: number;
  title: string | null;
  recruiter: string;
  position: string[];
}

@Component({
  selector: 'app-add-interview',
  templateUrl: './interview-add.component.html',
  styleUrls: ['./interview-add.component.css'],
  providers: [DatePipe]
})
export class InterviewAddComponent implements OnInit {
  interview: any = {
    title: '',
    candidateName: '',
    scheduleTime: '',
    fromTime: '',
    toTime: '',
    note: '',
    job: [], // Initialized as an empty array
    interviewers: [],
    location: '',
    recruiter: '',
    meetingId: '',
    result: 'NA',
    status: 'INVITED',
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
  PoLevel: string | null = null;
  constructor(
    private interviewService: InterviewService,
    private router: Router,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.interviewService.getAddInterview().subscribe(
      (data: any) => {
        this.candidates = data.candidate;
        this.jobs = data.job;
        this.interview.userinterviewers = data.userinterviewers;
        this.filteredJobTitles = [];
      },
      error => {
        this.errorMessage = 'Error loading initial data';
        console.error('Error loading initial data', error);
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
      console.log('Form is invalid. Error message:', this.errorMessage);
      return;
    }
    this.updateJobData();
    this.interview.job[0].position.push(this.PoLevel);
    // Update candidate list based on selected name
    this.interview.candidate = this.candidates.filter(candidate => candidate.name === this.interview.candidateName);


    this.interview.scheduleTime = this.datePipe.transform(this.interview.scheduleTime, 'yyyy-MM-dd') || '';

    console.log('Interview to be added:', this.interview);

    this.interviewService.addInterview(this.interview).subscribe(
      response => {
        this.router.navigate(['/interviews']);
      },
      error => {
        this.errorMessage = 'Error adding interview';
        console.error('Error adding interview', error);
      }
    );
  }


  onCancel() {
    this.router.navigate(['/interviews']);
  }

  updateRecruiter() {
    const selectedJob = this.interview.job.find((job: Job) => job.position.includes(this.selectedJobPosition || ''));
    if (selectedJob) {
      this.interview.recruiter = selectedJob.recruiter;
    } else {
      this.interview.recruiter = '';  // Clear recruiter if no job is selected
    }
  }

  validateScheduleDate(selectedDate: Date) {
    this.scheduleDateError = selectedDate <= this.today;
  }

  validateTimes() {
    const fromTime = new Date(`1970-01-01T${this.interview.fromTime}`);
    const toTime = new Date(`1970-01-01T${this.interview.toTime}`);
    this.timeError = fromTime >= toTime;
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
