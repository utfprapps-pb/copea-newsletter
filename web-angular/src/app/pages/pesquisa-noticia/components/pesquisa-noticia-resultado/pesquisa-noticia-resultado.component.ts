import { Component, Input, OnInit } from '@angular/core';

// noticia
import { Noticia } from 'src/app/pages/noticias/models/noticia';

@Component({
    selector: 'app-pesquisa-noticia-resultado',
    templateUrl: 'pesquisa-noticia-resultado.component.html'
})

export class PesquisaNoticiaResultadoComponent implements OnInit {

    @Input() public dto!: Noticia;

    constructor() { }

    ngOnInit() { }

}