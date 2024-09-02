import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CandidateService } from '../candidate.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../user.service'; // Import UserService
import { Skill } from '../skill.enum'; 
import { Job } from '../candidate.model';
import { AbstractControl, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-candidate-create',
  templateUrl: './candidate-create.component.html',
  styleUrls: ['./candidate-create.component.css']
})
export class CandidateCreateComponent implements OnInit {
  candidateForm: FormGroup;
  selectedFile: File | null = null;
  skillOptions = Object.keys(Skill); // Chuyển enum Skill thành mảng các giá trị
  recruiters: any[] = []; // Chứa danh sách recruiter


  constructor(
    private fb: FormBuilder,
    private candidateService: CandidateService,
    private userService: UserService, // Inject UserService
    private toastr: ToastrService,
    private router: Router
  ) {
    this.candidateForm = this.fb.group({
      fullName: ['', Validators.required],
      dob: ['',[Validators.required, this.pastDateValidator] ],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      position: ['', Validators.required],
      skills: this.fb.array([], atLeastOneSkillValidator()), // Sử dụng mảng cho skills
      recruiter: ['', Validators.required],
      note: [''],
      yearOfExperience: ['', [Validators.required, Validators.min(0)]],
      highestLevel: ['', Validators.required],
      job: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadRecruiters();
    this.initializeSkills();
  }

  loadRecruiters(): void {
    this.userService.getRecruiters().subscribe(
      (recruiters) => {
        this.recruiters = recruiters;
      },
      (error) => {
        this.toastr.error('Failed to load recruiters');
      }
    );
  }
  initializeSkills(): void {
    const skillsFormArray = this.candidateForm.get('skills') as FormArray;
    this.skillOptions.forEach(() => {
      skillsFormArray.push(this.fb.control(false));
    });
  }
  get skillsFormArray(): FormArray {
    return this.candidateForm.get('skills') as FormArray;
  }

  pastDateValidator(control: any): { [key: string]: boolean } | null {
    const today = new Date();
    const dob = new Date(control.value);
    if (dob >= today) {
      return { 'pastDate': true };
    }
    return null;
  }
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
  
      this.candidateService.uploadCv(formData).subscribe(
        (response: any) => {
          // Xử lý dữ liệu từ server
          this.candidateForm.patchValue({
            fullName: response.fullName,
            dob: response.dob,
            email: response.email,
            phoneNumber: response.phoneNumber,
            address: response.address,
            gender: response.gender,
          });
  
          // Xử lý danh sách job
          const jobArray = this.candidateForm.get('job') as FormArray;
          jobArray.clear(); // Xóa các job cũ
  
          // Thêm các job mới vào FormArray
          response.jobs.forEach((jobTitle: string) => {
            jobArray.push(this.fb.group({
              title: [jobTitle]
            }));
            console.log(jobTitle); // In ra giá trị của jobTitle
          });
        },
        error => {
          console.error('Error uploading file:', error);
          this.toastr.error('Error uploading file');
          // Handle error appropriately
        }
      );
    }
  }

  onSubmit(): void {
    if (this.candidateForm.valid) {
      const candidate = this.candidateForm.value;
      candidate.skills = this.skillOptions
      .filter((_, i) => this.skillsFormArray.at(i).value) // Kiểm tra các kiểm soát
      .map(skill => skill);

      console.log(candidate.skills);
      console.log(candidate);
      delete candidate.job;

      const recruiter = this.recruiters.find(r => r.fullName === candidate.recruiter);
      candidate.recruiter = { id: recruiter?.id };
      if (this.selectedFile) {
        this.candidateService.createCandidate(candidate, this.selectedFile).subscribe(
          response => {
            this.toastr.success('Candidate created successfully');
            this.router.navigate(['/candidates']);
          },
          error => {
            this.toastr.error('Failed to create candidate');
          }
        );
      } else {
        this.toastr.error('Please select a CV file');
      }
    }
  }

  cancel(): void {
    this.router.navigate(['/candidates']);
  }
  get jobFormArray(): FormArray {
    return this.candidateForm.get('job') as FormArray;
  }
}
// Validator function to check if at least one skill is selected
function atLeastOneSkillValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: boolean } | null => {
    if (control instanceof FormArray) {
      const valid = control.controls.some(ctrl => ctrl.value);
      return valid ? null : { 'atLeastOneSkill': true };
    }
    return null;
  };
}
