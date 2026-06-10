import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Residance } from './residance';

describe('Residance', () => {
  let component: Residance;
  let fixture: ComponentFixture<Residance>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Residance],
    }).compileComponents();

    fixture = TestBed.createComponent(Residance);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
