/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { UuidSelfRegistrationButtonsHandleComponent } from './uuid-self-registration-buttons-handle.component';

describe('UuidSelfRegistrationButtonsHandleComponent', () => {
  let component: UuidSelfRegistrationButtonsHandleComponent;
  let fixture: ComponentFixture<UuidSelfRegistrationButtonsHandleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UuidSelfRegistrationButtonsHandleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UuidSelfRegistrationButtonsHandleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
