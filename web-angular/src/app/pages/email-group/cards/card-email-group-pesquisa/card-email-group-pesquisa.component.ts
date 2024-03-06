import { Component, OnInit } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';
import { GrupoDestinatarioService } from 'src/app/pages/grupo-destinatarios/grupo-destinatario.service';
import { GrupoDestinatario } from 'src/app/pages/grupo-destinatarios/model/grupo-destinatario';
import { CrudController } from 'src/app/shared/crud/crud.controller';
import { errorTransform } from 'src/app/shared/pipes/error-transform';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-card-email-group-pesquisa',
  templateUrl: './card-email-group-pesquisa.component.html',
  styleUrls: ['./card-email-group-pesquisa.component.scss']
})
export class CardEmailGroupPesquisaComponent implements OnInit {

  /**
     * @description Filtro da tabela
     */
  public filtro: string;

  /**
     * @description Armazena a lista de registros filtrados
     */
  public listaRegistrosFiltro: GrupoDestinatario[];

  /**
     * @description Armazena a lista de registros retornada pela api
     */
  public listaRegistros: GrupoDestinatario[];

  /**
     * @description Armazena as colunas da tabela
     */
  public columns: string[] = ['id', 'name', 'uuidToSelfRegistration', 'acoes'];

  /**
     * @description Flag que identifica o estado de "carregamento"
     */
  public loading: boolean;

  /**
     * @description Evento de edição do registro
     */
  private _editarRegistroEvent: Subject<number> = new Subject();

  /**
   * @description Evento de remoção o registro
   */
  private _removerRegistroEvent: Subject<number> = new Subject();

  /**
   * @description Armazena as incrições de eventos do componente
   */
  private subscription: Subscription;

  constructor(
    private service: GrupoDestinatarioService,
    private controller: CrudController,
    private mensagemService: MensagemService,
  ) {
    this.subscription = new Subscription();
    this.listaRegistros = [];
    this.loading = false;
  }

  ngOnInit() {
    this.implementEvents();
    this.carregarRegistros();
  }

  private implementEvents() {
    // events do crud
    this.subscription.add(this.controller.onOperacaoConcluida.subscribe(() => this.carregarRegistros()));
  }

  /**
     * @description Filtra os registros da tabela (carregados)
     */
  public filtrarTabela() {
    if (this.filtro) {
      this.listaRegistrosFiltro = this.listaRegistros.filter(item =>
      ((item.id === +this.filtro) ||
        (item.name?.includes(this.filtro)) ||
        (item.uuidToSelfRegistration?.includes(this.filtro)))
      );
    } else {
      this.listaRegistrosFiltro = this.listaRegistros;
    }
  }

  /**
 * @description Busca os registros na api
 */
  public carregarRegistros() {
    this.loading = true;
    this.service.pesquisarTodos().subscribe(res => {
      this.loading = false;
      this.listaRegistros = res;
      this.filtrarTabela();
    }, error => {
      this.loading = false;
      this.mensagemService.mostrarMensagem(errorTransform(error));
    })
  }

  /**
 * @description Lança o evento de edição do registro
 * @param id Identificador do registro
 */
  public editarRegistro(id: number): void {
    this._editarRegistroEvent.next(id);
  }

  /**
* @description Lança o evento de remoção do registro
* @param id Identificador do registro
*/
  public removerRegistro(id: number): void {
    if (id && confirm('Você tem certeza que deseja remover o grupo? Essa ação não poderá ser desfeita.'))
      this._removerRegistroEvent.next(id);
  }

  public get editarRegistroEvent(): Observable<number> {
    return this._editarRegistroEvent.asObservable();
  }

  public get removerRegistroEvent(): Observable<number> {
    return this._removerRegistroEvent.asObservable();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public onNewUuid(uuid: string, grupo: GrupoDestinatario) {
    grupo.uuidToSelfRegistration = uuid;
    if (grupo.id)
      this.editarRegistro(grupo.id);
  }

  public onRemoveUuid(grupo: GrupoDestinatario) {
    grupo.uuidToSelfRegistration = '';
    this.mensagemService.mostrarMensagem('Link removido com sucesso.');
    if (grupo.id)
      this.editarRegistro(grupo.id);
  }

}
