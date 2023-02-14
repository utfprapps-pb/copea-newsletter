import { MensagemService } from './../../shared/services/mensagem.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// material
import { MatDialog } from '@angular/material/dialog';

// shared
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';

// aplicação
import { ConfigEmail } from './models/config-email';
import { ConfiguracaoService } from './configuracao.service';

@Component({
    selector: 'app-configuracao',
    templateUrl: './configuracao.component.html',
    providers: [CrudController]
})
export class ConfiguracaoComponent extends BasicCrudComponent<ConfigEmail> implements OnInit {

    constructor(
        public crudController: CrudController,
        public override formBuilder: FormBuilder,
        public override service: ConfiguracaoService,
        public override mensagemService: MensagemService,
        public override route: ActivatedRoute,
        public dialog: MatDialog,
    ) {
        super(crudController, formBuilder, service, mensagemService, route);
    }

    override ngOnInit() {
        super.ngOnInit();
    }

    public criarForm(): FormGroup<any> {
        return this.formBuilder.group({
            id: [null],
            emailFrom: [null, Validators.required],
            passwordEmailFrom: [null, Validators.required],
            sendHost: [null, Validators.required],
            sendPort: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
            user: [null],
        });
    }

    public override persistirAlteracoes(): void {
        super.persistirAlteracoes(this.form.get('id')!.value != null)
        this.dialog.closeAll();
    }

}
