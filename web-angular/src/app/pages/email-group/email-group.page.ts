import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CardEmailGroupEdicaoComponent } from 'src/app/pages/email-group/cards/card-email-group-edicao/card-email-group-edicao.component';
import { CardEmailGroupPesquisaComponent } from 'src/app/pages/email-group/cards/card-email-group-pesquisa/card-email-group-pesquisa.component';
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
export class EmailGroupPage extends BasicCrudComponent<GrupoDestinatario> implements OnInit, AfterViewInit {

  @ViewChild(CardEmailGroupEdicaoComponent)
  private cardEdicao!: CardEmailGroupEdicaoComponent;

  @ViewChild(CardEmailGroupPesquisaComponent)
  private cardPesquisa!: CardEmailGroupPesquisaComponent;

  /**
     * @description Armazena as incrições de eventos do componente
     */
  private subscription: Subscription;

  constructor(
    public crudController: CrudController,
    public override formBuilder: FormBuilder,
    public override service: GrupoDestinatarioService,
    public override mensagemService: MensagemService,
    public override route: ActivatedRoute,
  ) {
    super(crudController, formBuilder, service, mensagemService, route);
    this.subscription = new Subscription();
  }

  ngAfterViewInit(): void {
    super.ngOnInit();
    this.implementEvents();
  }

  public override criarForm(): FormGroup<any> {
    return this.formBuilder.group({
      id: [null],
      name: [null, Validators.required],
      uuidToSelfRegistration: [null],
    });
  }

  private implementEvents() {
    // events do card de edição
    this.subscription.add(this.cardEdicao.persistirEdicaoEvent.subscribe(() => super.persistirAlteracoes(this.form.get('id')?.value != null)));
    this.subscription.add(this.cardEdicao.removerRegistroEvent.subscribe(this.removerRegistro.bind(this)));
    this.subscription.add(this.cardEdicao.resetFormNovoEvent.subscribe(this.resetFormNovo.bind(this)));
    // events do card de pesquisa
    this.subscription.add(this.cardPesquisa.editarRegistroEvent.subscribe(super.carregar.bind(this)));
    this.subscription.add(this.cardPesquisa.removerRegistroEvent.subscribe(this.removerRegistro.bind(this)));
  }

  /**
 * @description Remove o registro e configura o card de edição (se necessário)
 * @param id Id do registro
 */
  private removerRegistro(id: number) {
    super.remover(id);

    if (this.form.get('id')?.value === id) {
      this.form.reset();
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public onCarregarRegistrosPesquisa() {
    this.cardPesquisa.carregarRegistros();
  }

}
