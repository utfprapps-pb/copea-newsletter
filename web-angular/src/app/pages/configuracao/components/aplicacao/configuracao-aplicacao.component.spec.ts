/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ConfiguracaoAplicacaoComponent } from './configuracao-aplicacao.component';

describe('ConfiguracaoAplicacaoComponent', () => {
  let component: ConfiguracaoAplicacaoComponent;
  let fixture: ComponentFixture<ConfiguracaoAplicacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfiguracaoAplicacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfiguracaoAplicacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
