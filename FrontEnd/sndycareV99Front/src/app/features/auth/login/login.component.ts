import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/service/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true
})
export class Login {
  loginForm: FormGroup;
  loadingForm = false;
  error = '';

  constructor(
    private fb: FormBuilder, 
    private auth: AuthService, 
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loadingForm = true;
    this.error = '';

    const value = this.loginForm.value;
    const email = (value.email ?? '').toString().trim();
    if (!email) {
      this.loadingForm = false;
      this.error = 'Email is required.';
      return;
    }

    const payload = { email, password: value.password };
  
    this.auth.login(payload).subscribe({
      next: (_response: any) => {
        this.loadingForm = false;
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        this.loadingForm = false;
        console.error('Login failed', err);
        this.error = err?.error?.message || err?.message || 'Invalid credentials';
      }
    });
  }
}