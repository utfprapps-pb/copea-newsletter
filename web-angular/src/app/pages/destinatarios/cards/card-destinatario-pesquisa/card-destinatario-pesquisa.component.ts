import { MensagemService } from './../../../../shared/services/mensagem.service';

import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';

// shared
import { CrudController } from 'src/app/shared/crud/crud.controller';
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { DestinatarioService } from '../../destinatario.service';
import { Destinatario } from '../../model/destinatario';

@Component({
    selector: 'app-card-destinatario-pesquisa',
    templateUrl: 'card-destinatario-pesquisa.component.html',
    styleUrls: ['./card-destinatario-pesquisa.component.scss']
})
export class CardDestinatarioPesquisaComponent implements OnInit, OnDestroy {

    /**
     * @description Armazena a lista de registros retornada pela api
     */
    public listaRegistros: Destinatario[];

    /**
     * @description Armazena a lista de registros filtrados
     */
     public listaRegistrosFiltro: Destinatario[];

    /**
     * @description Flag que identifica o estado de "carregamento"
     */
    public loading: boolean;

    /**
     * @description Armazena as colunas da tabela
     */
    public columns: string[] = ['id', 'description', 'emailGroupRelations', 'acoes'];

    /**
     * @description Filtro da tabela
     */
    public filtro: string;

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
        private service: DestinatarioService,
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
     * @description Filtra os registros da tabela (carregados)
     */
    public filtrarTabela() {
        if (this.filtro) {
            this.listaRegistrosFiltro = this.listaRegistros.filter(item =>
                item.id === +this.filtro ||
                item.email?.includes(this.filtro) ||
                item.emailGroupRelations?.find(grupo => grupo.emailGroup?.name?.includes(this.filtro))
            );
        } else {
            this.listaRegistrosFiltro = this.listaRegistros;
        }
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
        if (id && confirm('Você tem certeza que deseja remover o destinatário? Essa ação não poderá ser desfeita.'))
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
}
