import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InterviewService } from '../interview.service';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

interface InterviewDetail {
  title: string;
  candidateName: string;
  scheduleTime: string;
  fromTime: string;
  toTime: string;
  note: string;
  positionLevel: string;
  interviewers: { name: string }[]; // Ensure this is defined and not undefined
  location: string;
  recruiter: string;
  meetingId: string;
  result: string;
  status: string;
}

@Component({
  selector: 'app-interview-send',
  templateUrl: './interview-send.component.html',
  styleUrls: ['./interview-send.component.css']
})
export class InterviewSendComponent implements OnInit {
 interviewDetail : InterviewDetail = {
    title: '',
    candidateName: '',
    scheduleTime: '',
    fromTime: '',
    toTime: '',
    note: '',
    positionLevel: '',
    interviewers: [], // Ensure this is defined and not undefined
    location: '',
    recruiter: '',
    meetingId: '',
    result: '',
    status: '',
  };

  constructor(
    private route: ActivatedRoute,
    private interviewService: InterviewService,
    private location: Location,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.interviewService.getInterviewById(id).subscribe(
        data => {
          this.interviewDetail = data;
        },
        error => {
          console.error('Error occurred:', error);
          alert('Failed to load interview details');
        }
      );
    }
  }

  sendEmail(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const param = {
      result: this.interviewDetail.result ,
      note: this.interviewDetail.note  // Giá trị từ thẻ <textarea> này
    };
    this.interviewService.send(Number(id),param).subscribe(
      success => {
        console.log('Email sent:', success);
        if (success == true) {
          alert('Email sent successfully');
        } else {
          alert('Failed to send email');
        }
      },
      error => {
        console.error('Error occurred:', error);
        alert('Failed to send email');
      }
    );
  }

  goBack(): void {
    this.location.back();
  }

  formatInterviewers(interviewers: { name: string }[] = []): string {
    return interviewers.map(interviewer => interviewer.name).join(', ');
  }

  onResultChange(result: 'PASSED' | 'FAILED'): void {
    if (this.interviewDetail) {
      this.interviewDetail.result = result;
      this.interviewDetail.status = 'INTERVIEWED'; // Update status when result is changed
    }
  }
}
