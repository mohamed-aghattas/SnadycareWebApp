import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Residence } from './residence';

describe('Residence', () => {
  let component: Residence;
  let fixture: ComponentFixture<Residence>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Residence],
    }).compileComponents();

    fixture = TestBed.createComponent(Residence);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
