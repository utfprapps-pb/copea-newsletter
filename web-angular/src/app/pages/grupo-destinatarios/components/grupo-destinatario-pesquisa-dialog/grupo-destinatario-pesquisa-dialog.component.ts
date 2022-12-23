import { MensagemService } from './../../../../shared/services/mensagem.service';
import { Component, OnInit } from '@angular/core';

// material
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

// shared
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { GrupoDestinatarioService } from '../../grupo-destinatario.service';
import { GrupoDestinatario } from '../../model/grupo-destinatario';
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';

@Component({
    selector: 'app-grupo-destinatario-pesquisa-dialog',
    templateUrl: './grupo-destinatario-pesquisa-dialog.component.html',
    styleUrls: ['./grupo-destinatario-pesquisa-dialog.component.scss'],
    providers: [
        GrupoDestinatarioService,
        DestinatarioService
    ]
})
export class GrupoDestinatarioPesquisaDialogComponent implements OnInit {

    /**
     * @description Código do grupo selecionado no mat-select
     */
    public selecao: number;

    /**
     * @description Opções de grupos
     */
    public options: GrupoDestinatario[];

    // flags
    public loading: boolean;

    constructor(
        private destinatarioService: DestinatarioService,
        public matDialogRef: MatDialogRef<GrupoDestinatarioPesquisaDialogComponent>,
        public service: GrupoDestinatarioService,
        private mensagemService: MensagemService,
        public dialog: MatDialog,
    ) { }

    ngOnInit() {
        this.loading = true;
        this.service.pesquisarTodos().subscribe(res => {
            this.loading = false;
            this.options = res;
        }, error => {
            this.loading = false;
            this.mensagemService.mostrarMensagem(errorTransform(error));
        });
    }

    public importarGrupo() {
        if (!this.selecao) {
            return;
        }

        this.loading = true;
        this.destinatarioService.buscarPorGrupo(this.selecao).subscribe(res => {
            this.loading = false;
            this.matDialogRef.close(res);
        }, error => {
            this.loading = false;
            this.mensagemService.mostrarMensagem(errorTransform(error));
        });
    }

}
