import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ConfigEmail } from 'src/app/pages/configuracao/components/email/models/config-email';
import { ConfiguracaoEmailService } from 'src/app/pages/configuracao/components/email/services/configuracao-email.service';
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-configuracao-email',
  templateUrl: './configuracao-email.component.html',
  styleUrls: ['./configuracao-email.component.css'],
  providers: [
    CrudController,
  ]
})
export class ConfiguracaoEmailComponent extends BasicCrudComponent<ConfigEmail> implements OnInit {

  constructor(
    public crudController: CrudController,
    public override formBuilder: FormBuilder,
    public override service: ConfiguracaoEmailService,
    public override mensagemService: MensagemService,
    public override route: ActivatedRoute,
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
  }

}
