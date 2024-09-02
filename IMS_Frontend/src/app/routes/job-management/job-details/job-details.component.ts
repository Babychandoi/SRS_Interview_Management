import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobService } from '../job.service';
import { ActivatedRoute } from '@angular/router';
import { Job } from '../job.model';
import { benefitOptions, levelOptions, skillOptions } from '../options';

@Component({
  selector: 'app-job-details',
  templateUrl: './job-details.component.html',
  styleUrls: ['./job-details.component.css']
})
export class JobDetailsComponent implements OnInit {
  job: any;
  skillOptions = skillOptions;
  levelOptions = levelOptions;
  benefitOptions = benefitOptions;

  constructor(private jobService: JobService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((res) => {
      const jobId = res.get('id');
      if (jobId) {
        this.jobService.getJobById(jobId).subscribe({
          next: (res) => {
            this.job = res.data;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.log(err);
          }
        })
      }
    })
  }

  getValue(type: number, key: string): string {
    let options;
    if (type === 1) {
      options = this.benefitOptions;
    } else if (type === 2) {
      options = this.levelOptions;
    } else {
      options = this.skillOptions;
    }
    const res = options.find(x => x.key === key);
    return res ? res.value : key;
  }
}
