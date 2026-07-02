import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feature-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feature-page.html',
  styleUrls: ['./feature-page.scss'],
})
export class FeaturePage {
  title = '';
  subtitle = '';

  constructor(private route: ActivatedRoute) {
    const data = route.snapshot.data;
    this.title = data['title'] || 'Feature';
    this.subtitle = data['subtitle'] || '';
  }
}
