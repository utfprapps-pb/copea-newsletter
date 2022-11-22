import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardSelecionarNoticiaModeloComponent } from './card-selecionar-noticia-modelo.component';

describe('CardSelecionarNoticiaModeloComponent', () => {
  let component: CardSelecionarNoticiaModeloComponent;
  let fixture: ComponentFixture<CardSelecionarNoticiaModeloComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardSelecionarNoticiaModeloComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardSelecionarNoticiaModeloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
