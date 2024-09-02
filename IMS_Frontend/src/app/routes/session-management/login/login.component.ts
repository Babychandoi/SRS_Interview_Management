import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {

  }

  login() {
    if (this.loginForm.valid) {
      const username = this.loginForm.value.email;
      const password = this.loginForm.value.password;
      this.authService.login(username, password).subscribe({
        next: _ => {
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.log(err)
        }
      })
    }
  }
}
