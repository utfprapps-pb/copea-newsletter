import { MensagemService } from './../../../../shared/services/mensagem.service';
import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// material
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

// shared
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';

// aplicação
import { GrupoDestinatarioService } from '../../grupo-destinatario.service';
import { GrupoDestinatario } from '../../model/grupo-destinatario';

@Component({
    selector: 'app-grupo-destinatario-dialog',
    templateUrl: './grupo-destinatario-dialog.component.html',
    styleUrls: ['./grupo-destinatario-dialog.component.scss'],
    providers: [
        GrupoDestinatarioService,
        CrudController
    ]
})
export class GrupoDestinatarioDialogComponent extends BasicCrudComponent<GrupoDestinatario> implements OnInit {

    constructor(
        @Inject(MAT_DIALOG_DATA) private data: { registro: GrupoDestinatario },
        public crudController: CrudController,
        public override formBuilder: FormBuilder,
        public override service: GrupoDestinatarioService,
        public override mensagemService: MensagemService,
        public override route: ActivatedRoute,
        public dialog: MatDialog,
    ) {
        super(crudController, formBuilder, service, mensagemService, route);
    }

    override ngOnInit() {
        super.ngOnInit();

        if (this.data && this.data.registro) {
            this.form.reset(this.data.registro);
        }
    }

    public get id(): number {
        return this.form.get('id')!.value;
    }

    public criarForm(): FormGroup<any> {
        return this.formBuilder.group({
            id: [null],
            name: [null, Validators.required],
        });
    }

    public override persistirAlteracoes(): void {
        super.persistirAlteracoes(this.id != null)
        this.dialog.closeAll();
    }

    /**
     * @description Executa no click do botão excluir
     */
     public onClickRemover() {
        if (this.id && confirm('Você tem certeza que deseja remover o grupo? Essa ação não poderá ser desfeita.')) {
            super.remover(this.id);
        }
    }

}
