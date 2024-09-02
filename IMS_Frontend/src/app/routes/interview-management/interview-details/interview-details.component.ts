import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InterviewService } from '../interview.service';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
@Component({
  selector: 'app-interview-detail',
  templateUrl: './interview-details.component.html',
  styleUrls: ['./interview-details.component.css']
})
export class InterviewDetailsComponent implements OnInit {
  interviewDetail: any;

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
        }
      );
    }
  }
  sendEmail(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.interviewService.sendMail(Number(id)).subscribe(
      data => {
        if (data == true) {
          alert('Gửi email thành công');
        } else {
          alert('Gửi email thất bại');
        }
      }
    );

  }
  edit(): void {
    this.router.navigate(['/edit-interview', this.interviewDetail.id]);
  }
  goBack(): void {
    this.location.back();
  }

  formatInterviewers(interviewers: any[]): string {
    return interviewers.map(interviewer => `${interviewer.name}`).join(', ');
  }


  formatSchedule(element: any): string {
    return `${element.scheduleTime} từ ${element.fromTime} đến ${element.toTime}`;
  }
}
