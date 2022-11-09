import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { AdvancedCrudComponent } from 'src/app/shared/crud/advanced-crud-component';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { Noticia } from './models/noticia';
import { NoticiaService } from './noticia.service';

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

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override service: NoticiaService,
    public override snackBar: MatSnackBar,
    public override route: ActivatedRoute,
  ) {
    super(crudController, service, snackBar, route);
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

    this.service.enviarNoticia(this.registro.id!).subscribe(res => {
      this.snackBar.open('A notícia foi enviada com sucesso!', 'OK');
    }, error => {
      this.snackBar.open(errorTransform(error) + '', 'OK');
    })
  }

  /**
   * @description Executa no click do botão salvar
   */
  public onClickSalvar() {
    super.persistirAlteracoes(this.registro.id != null);
  }

  /**
   * @description Executa no click do botão excluir
   */
  public onClickRemover() {
    if (this.registro.id && confirm('Você tem certeza que deseja remover o noticia? Essa ação não poderá ser desfeita.')) {
      super.remover(this.registro.id);
    }
  }

}
