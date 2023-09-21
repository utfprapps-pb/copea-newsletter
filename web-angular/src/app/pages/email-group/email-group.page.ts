import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GrupoDestinatarioService } from 'src/app/pages/grupo-destinatarios/grupo-destinatario.service';
import { GrupoDestinatario } from 'src/app/pages/grupo-destinatarios/model/grupo-destinatario';
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-email-group',
  templateUrl: './email-group.page.html',
  styleUrls: ['./email-group.page.css'],
  providers: [
    GrupoDestinatarioService,
    CrudController
  ],
})
export class EmailGroupPage extends BasicCrudComponent<GrupoDestinatario> implements OnInit {

  constructor(
    public crudController: CrudController,
    public override formBuilder: FormBuilder,
    public override service: GrupoDestinatarioService,
    public override mensagemService: MensagemService,
    public override route: ActivatedRoute,
  ) {
    super(crudController, formBuilder, service, mensagemService, route);
  }

  public override criarForm(): FormGroup<any> {
    return this.formBuilder.group({
      id: [null],
      name: [null, Validators.required],
      uuidToSelfRegistration: [null],
    });
  }

}
