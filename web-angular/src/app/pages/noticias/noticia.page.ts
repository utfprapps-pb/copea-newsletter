import { MensagemService } from './../../shared/services/mensagem.service';
import { CardSelecionarNoticiaModeloComponent } from './cards/card-selecionar-noticia-modelo/card-selecionar-noticia-modelo.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// shared
import { AdvancedCrudComponent } from 'src/app/shared/crud/advanced-crud-component';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { Noticia } from './models/noticia';
import { NoticiaService } from './noticia.service';
import { LastSentEmailNewsletter } from './models/lastSentEmailNewsletter';

@Component({
  selector: 'app-noticia',
  templateUrl: 'noticia.page.html',
  styleUrls: ['./noticia.page.scss'],
  providers: [
    AdvancedCrudController,
    NoticiaService,
  ]
})
export class NoticiaComponent extends AdvancedCrudComponent<Noticia> implements OnInit {

  public lastSentEmailNewsletter: LastSentEmailNewsletter;

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override service: NoticiaService,
    public override mensagemService: MensagemService,
    public override route: ActivatedRoute,
    public dialog: MatDialog,
  ) {
    super(crudController, service, mensagemService, route);
  }

  override ngOnInit() {
    super.ngOnInit();
  }

  /**
   * @description Executa no click do botão "enviar"
   */
  public onClickEnviar() {
    // Comentado pois o TinyMCE editor está ficando sempre como dirty
    // if (super.possuiAlteracoesPendentes()) {
    //     this.snackBar.open('Existem alterações pendentes! Por favor, salve o registro antes de enviá-lo.', 'OK');
    //     return;
    // }

    this.setLoading(true);
    this.service.enviarNoticia(this.registro.id!).subscribe(res => {
      this.getLastSentEmail(this.registro.id!);
      this.mensagemService.mostrarMensagem('A newsletter foi enviada com sucesso!');
      this.setLoading(false);
    }, error => {
      this.mensagemService.mostrarMensagem(errorTransform(error) + '');
      this.setLoading(false);
    })
  }

  private setLoading(loading: boolean) {
    this.loading = loading;
  }

  /**
   * @description Executa no click do botão salvar
   */
  public onClickSalvar() {
    super.persistirAlteracoes(this.registro.id != null);
  }

  /**
   * @description Executa a validação do form e persiste o registro no banco
   * @param registro Registro que será persistido
   */
  public override salvar(registro: Noticia): void {
    if (this.validarForm()) {
      this.setLoading(true);
      this.service.incluir(registro).subscribe((res: { message: string }) => {
        this.setLoading(false);
        this.mensagemService.mostrarMensagem('O registro foi incluído com sucesso!');

        if (res && res.message && +res.message >= 1) {
          this.carregar(+res.message);
        } else {
          this.resetFormNovo();
        }
      }, error => {
        console.log(error);
        this.setLoading(false);
        this.mensagemService.mostrarMensagem(errorTransform(error) + '');
      });
    }
  }

  /**
   * @description Executa no click do botão excluir
   */
  public onClickRemover() {
    if (this.registro.id && confirm('Você tem certeza que deseja remover o noticia? Essa ação não poderá ser desfeita.')) {
      super.remover(this.registro.id);
    }
  }

  public abrirCardSelecionarNoticiaModelo() {
    this.criarEventAoFecharDialogSelecaoNoticiaModelo(this.dialog.open(CardSelecionarNoticiaModeloComponent, { width: '900px', maxHeight: '500px' }));
  }

  public criarEventAoFecharDialogSelecaoNoticiaModelo(dialogRef: MatDialogRef<CardSelecionarNoticiaModeloComponent>) {
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (!value)
          return;

        this.registro.newsletter = value.newsletter;
        this.updateValueForm('newsletter', this.registro.newsletter!);
      }
    })
  }

  public override carregar(registroId: number): void {
    super.carregar(registroId);
    this.getLastSentEmail(registroId);
  }

  public getLastSentEmail(registroId) {
    this.lastSentEmailNewsletter = {};
    this.service.getLastSentEmail(registroId).subscribe(
      {
        next: (value) => {
          this.lastSentEmailNewsletter = value;
        },
      }
    )
  }

}
