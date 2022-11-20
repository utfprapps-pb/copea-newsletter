import { Component, OnInit } from '@angular/core';

// material
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

// shared
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { GrupoDestinatarioService } from '../../grupo-destinatario.service';
import { GrupoDestinatario } from '../../model/grupo-destinatario';

@Component({
    selector: 'app-grupo-destinatario-pesquisa-dialog',
    templateUrl: './grupo-destinatario-pesquisa-dialog.component.html',
    styleUrls: ['./grupo-destinatario-pesquisa-dialog.component.scss'],
    providers: [
        GrupoDestinatarioService,
    ]
})
export class GrupoDestinatarioPesquisaDialogComponent implements OnInit {

    /**
     * @description Grupo selecionado no mat-select 
     */
    public selecao: GrupoDestinatario;

    /**
     * @description Opções de grupos
     */
    public options: GrupoDestinatario[];

    // flags
    public loading: boolean;

    constructor(
        public service: GrupoDestinatarioService,
        public snackBar: MatSnackBar,
        public dialog: MatDialog,
    ) { }

    ngOnInit() {
        this.loading = true;
        this.service.pesquisarTodos().subscribe(res => {
            this.loading = false;
            this.options = res;
        }, error => {
            this.loading = false;
            this.snackBar.open(errorTransform(error), 'Ok');
        })
    }

    public importarGrupo() {
        this.dialog.closeAll();
    }

}