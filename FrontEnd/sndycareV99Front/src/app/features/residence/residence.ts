import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ResidenceRequest } from '../../core/models';

@Component({
  selector: 'app-residence',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './residence.html',
  styleUrls: ['./residence.scss'],
})
export class Residence {
  residenceForm: FormGroup;
  isEditMode = false;
  isLoading = false;
  serverErrorMessage = '';

  constructor(private fb: FormBuilder) {
    this.residenceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      numberUnits: [1, [Validators.required, Validators.min(1)]],
      city: ['', Validators.required],
      address: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  onSubmit() {
    if (this.residenceForm.invalid) {
      this.residenceForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.serverErrorMessage = '';

    const payload = this.residenceForm.value as ResidenceRequest;

    // Simulate async save (replace with HTTP call to backend when available)
    setTimeout(() => {
      try {
        const key = 'residences';
        const existing = JSON.parse(localStorage.getItem(key) || '[]');
        existing.push({ ...payload, createdAt: new Date().toISOString() });
        localStorage.setItem(key, JSON.stringify(existing));

        this.isLoading = false;
        this.isEditMode = false;
        this.residenceForm.reset({ name: '', numberUnits: 1, city: '', address: '' });
      } catch (err: any) {
        this.isLoading = false;
        this.serverErrorMessage = 'Failed to save residence. Please try again.';
      }
    }, 500);
  }

  onCancel() {
    this.isEditMode = false;
    this.serverErrorMessage = '';
    this.residenceForm.reset({ name: '', numberUnits: 1, city: '', address: '' });
  }
}
