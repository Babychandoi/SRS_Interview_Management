import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterviewSendComponent } from './interview-send.component';

describe('InterviewSendComponent', () => {
  let component: InterviewSendComponent;
  let fixture: ComponentFixture<InterviewSendComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InterviewSendComponent]
    });
    fixture = TestBed.createComponent(InterviewSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
