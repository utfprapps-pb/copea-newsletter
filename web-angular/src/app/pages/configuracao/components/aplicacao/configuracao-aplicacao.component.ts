import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ConfigApplication } from 'src/app/pages/configuracao/components/aplicacao/models/config-application';
import { ConfiguracaoAplicacaoService } from 'src/app/pages/configuracao/components/aplicacao/services/configuracao-aplicacao.service';
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-configuracao-aplicacao',
  templateUrl: './configuracao-aplicacao.component.html',
  styleUrls: ['./configuracao-aplicacao.component.css'],
  providers: [
    CrudController,
  ]
})
export class ConfiguracaoAplicacaoComponent extends BasicCrudComponent<ConfigApplication> implements OnInit {

  constructor(
    public crudController: CrudController,
    public override formBuilder: FormBuilder,
    public override service: ConfiguracaoAplicacaoService,
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
      urlWeb: [null, Validators.required],
    });
  }

  public override persistirAlteracoes(): void {
    super.persistirAlteracoes(this.form.get('id')!.value != null)
  }

}
