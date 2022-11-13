import { Component, OnInit } from '@angular/core';

// material
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

// aplicação
import { GrupoDestinatarioService } from '../../grupo-destinatario.service';

@Component({
    selector: 'app-grupo-destinatario-pesquisa-dialog',
    templateUrl: './grupo-destinatario-pesquisa-dialog.component.html',
    styleUrls: ['./grupo-destinatario-pesquisa-dialog.component.scss'],
    providers: [
        GrupoDestinatarioService,
    ]
})
export class GrupoDestinatarioPesquisaDialogComponent implements OnInit {

    constructor(
        public service: GrupoDestinatarioService,
        public snackBar: MatSnackBar,
        public dialog: MatDialog,
    ) { }

    ngOnInit() { }

    public importarGrupo() {
        // TODO: importar
        this.dialog.closeAll();
    }

}