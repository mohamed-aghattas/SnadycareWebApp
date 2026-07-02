import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./loading-spinner.scss'],
  template :
  `<div [ngClass]="isOverlay ? 'spinner-overlay' : 'loading-state'">
  <div class="spinner-container">
    <i class="ti ti-loader-2 spin" style="font-size: 24px; color: var(--brand);"></i>
    <span *ngIf="message" class="text-muted font-medium">{{ message }}</span>
  </div>
</div>`
})
export class LoadingSpinnerComponent {
  
  @Input() message: string = 'Loading dashboard…';
  
  @Input() isOverlay: boolean = false;
}