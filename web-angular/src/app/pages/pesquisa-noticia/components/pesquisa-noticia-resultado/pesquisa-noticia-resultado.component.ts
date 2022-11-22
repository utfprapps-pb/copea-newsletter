import { CardSelecionarNoticiaModeloComponent } from './../../../noticias/cards/card-selecionar-noticia-modelo/card-selecionar-noticia-modelo.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Component, Input, OnInit, Optional } from '@angular/core';

// noticia
import { Noticia } from 'src/app/pages/noticias/models/noticia';

@Component({
  selector: 'app-pesquisa-noticia-resultado',
  templateUrl: 'pesquisa-noticia-resultado.component.html'
})

export class PesquisaNoticiaResultadoComponent implements OnInit {

  @Input() public dto!: Noticia;

  @Input() public pesquisaNoticiasModelos: Boolean = false;

  constructor(@Optional() public dialogRef: MatDialogRef<CardSelecionarNoticiaModeloComponent>) { }

  ngOnInit() {
  }

  closeDialogIfOpened() {
    if (this.dialogRef)
      this.dialogRef.close(this.dto);
  }

}
