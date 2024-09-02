import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CandidateService } from '../candidate.service';
import { UserService } from '../user.service'; // Import UserService
import { ToastrService } from 'ngx-toastr';
import { Skill } from '../skill.enum'; 
import { CandidateStatus } from '../status.enum'; 
import { Candidate } from '../candidate.model'; // Import Candidate model
import { AbstractControl, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-candidate-edit',
  templateUrl: './candidate-edit.component.html',
  styleUrls: ['./candidate-edit.component.css']
})
export class CandidateEditComponent implements OnInit {
  candidateForm: FormGroup;
  selectedFile: File | null = null;
  skillOptions = Object.values(Skill); // Example skill options
  recruiters: any[] = []; // Chứa danh sách recruiter
  candidateId!: number; // Candidate ID from route
  statusOptions = Object.values(CandidateStatus);
  cvUrl: string = '';
  errorMessages: any = {};

  constructor(
    private fb: FormBuilder,
    private candidateService: CandidateService,
    private userService: UserService, // Inject UserService
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute // Inject ActivatedRoute
  ) {
    this.candidateForm = this.fb.group({
      fullName: ['', Validators.required],
      dob: ['', [Validators.required, this.pastDateValidator]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      position: ['', Validators.required],
      skills: this.fb.array([],  atLeastOneSkillValidator()),
      recruiter: ['', Validators.required],
      note: [''],
      status: [''],
      yearOfExperience: ['', [Validators.required, Validators.min(0)]],
      highestLevel: ['', Validators.required],
      cvUrl: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.candidateId = +params['id']; // Get ID from route params
      if (this.candidateId) {
        this.loadCandidate();
      }
    });
    this.loadRecruiters();
    this.initializeSkills();
  }
  initializeSkills(): void {
    const skillsFormArray = this.candidateForm.get('skills') as FormArray;
    // Clear existing form controls
    skillsFormArray.clear();

  console.log('Skill Options:', this.skillOptions);
  console.log('Candidate Skills:', this.candidateForm.value.skills);
    
  const selectedSkills = this.candidateForm.value.skills || [];

  this.skillOptions.forEach(skill => {
    const isChecked = selectedSkills.includes(skill);
    skillsFormArray.push(this.fb.control(isChecked));
  });
    console.log('Skills Form Array:', skillsFormArray.value);
  }
  

  loadCandidate(): void {
    this.candidateService.getCandidateById(this.candidateId).subscribe(
      (candidate: Candidate) => {
        console.log('Candidate Data:', candidate);
        this.candidateForm.patchValue({
          fullName: candidate.fullName,
          dob: candidate.dob,
          phoneNumber: candidate.phoneNumber,
          email: candidate.email,
          address: candidate.address,
          gender: candidate.gender,
          position: candidate.position,
          note: candidate.note,
          status: candidate.status,
          yearOfExperience: candidate.yearOfExperience,
          highestLevel: candidate.highestLevel,
          recruiter: candidate.recruiterName,
          cvUrl: candidate.cvUrl
        });
        this.cvUrl = `http://localhost:8080/api/files/${encodeURIComponent(candidate.cvUrl.trim()).replace("uploads%2F","")}`;
         console.log(this.cvUrl)

        // Initialize skills after patching the form
        this.initializeSkills(); // Đảm bảo gọi sau khi patch dữ liệu
        this.setSkills(candidate.skills);
      },
      (error) => {
        this.toastr.error('Failed to load candidate data');
      }
    );
    
  }
  setSkills(skills: string[]): void {
    const skillsFormArray = this.candidateForm.get('skills') as FormArray;
    this.skillOptions.forEach((skill, index) => {
      skillsFormArray.at(index).setValue(skills.includes(skill));
    });
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
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    } else {
      this.selectedFile = null;
    }
  }
  
  onSubmit(): void {
    if (this.candidateForm.valid) {
      const candidate = this.candidateForm.value;
      delete candidate.status;
      candidate.skills = this.skillOptions
        .filter((_, i) => this.skillsFormArray.at(i).value)
        .map(skill => skill);

        const recruiter = this.recruiters.find(r => r.fullName === candidate.recruiter);
        candidate.recruiter = { id: recruiter?.id };
        
        console.log(candidate)
      if (this.selectedFile) {
        this.candidateService.updateCandidate(this.candidateId, candidate, this.selectedFile).subscribe(
          response => {
            this.toastr.success('Candidate updated successfully');
            this.router.navigate(['/candidates']);
          },
          error => {
           
            this.handleError(error);
          }
        );
      } else {
         // If no new file is selected, keep the old CV URL
         candidate.cvUrl = this.candidateForm.get('cvUrl')?.value;
         console.log('CV UR:', candidate.cvUrl)
         this.candidateService.updateCandidate(this.candidateId, candidate).subscribe(
           response => {
             this.toastr.success('Candidate updated successfully');
             this.router.navigate(['/candidates']);
           },
           error => {
             
             this.handleError(error);
           }
         );
      }
    }
  }

  handleError(error: any): void {
    if (error.status === 400 && error.error) {
      this.errorMessages = error.error.split('\n').reduce((acc: any, errorMsg: string) => {
        const [field, message] = errorMsg.split(':');
        acc[field] = message;
        return acc;
      }, {});
    } else {
      this.toastr.error('An unexpected error occurred');
    }
  }

  cancel(): void {
    this.router.navigate(['/candidates']);
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
