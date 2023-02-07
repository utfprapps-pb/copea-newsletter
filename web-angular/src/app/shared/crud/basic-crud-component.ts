import { MensagemService } from './../services/mensagem.service';
import { Injectable, OnInit } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";

// shared
import { errorTransform } from "../pipes/error-transform";

// aplicação
import { CrudService } from "./crud.service";
import { CrudComponent } from "./crud-component";
import { CrudController } from "./crud.controller";

@Injectable()
export abstract class BasicCrudComponent<T> implements CrudComponent<T>, OnInit {

    /**
     * @description Formulário do componente
     */
    public form: FormGroup;

    /**
     * @description Flag que identifica o estado de "carregamento"
     */
    public loading: boolean;

    constructor(
        public controller: CrudController,
        public formBuilder: FormBuilder,
        public service: CrudService<T>,
        public mensagemService: MensagemService,
        public route: ActivatedRoute,
    ) {
        this.form = this.criarForm();
        this.loading = false;
    }

    ngOnInit(): void {
        this.verificarCrudRouteParams();
    }

    /**
     * @description Cria o form do CRUD
     */
    public abstract criarForm(): FormGroup;

    /**
     * @description Cria o form do CRUD
     */
    public verificarCrudRouteParams() {
        const registroId = this.route.snapshot.paramMap.get('id');

        if (registroId) {
            // carrega o registro
            this.carregar(+registroId);
        } else {
            // prepara o form para uma nova inclusão
            this.loading = true;
            this.resetFormNovo();
        }
    }

    /**
     * @description Persiste as alteração da inclusão/edição
     * @param isEdicao Flag que indica Update ao invés de Create
     */
    public persistirAlteracoes(isEdicao: boolean): void {
        if (isEdicao) {
            this.atualizar(this.form.getRawValue());
        } else {
            this.salvar(this.form.getRawValue());
        }
    }

    /**
     * @description Executa a validação do form e persiste o registro no banco
     * @param registro Registro que será persistido
     */
    public salvar(registro: T): void {
        if (this.validarForm()) {
            this.loading = true;
            this.service.incluir(registro).subscribe(() => {
                this.loading = false;
                this.mensagemService.mostrarMensagem('O registro foi incluído com sucesso!');
                this.controller.notificarOperacaoConcluida();
                this.resetFormNovo();
            }, error => {
                this.loading = false;
                this.mensagemService.mostrarMensagem(errorTransform(error));
            });
        }
    }

    /**
     * @description Executa a validação do form e atualiza o registro do banco
     * @param registro Registro que será atualizado
     */
    public atualizar(registro: T): void {
        if (this.validarForm()) {
            this.loading = true;
            this.service.atualizar(registro).subscribe(() => {
                this.loading = false;
                this.mensagemService.mostrarMensagem('As alterações foram salvas com sucesso!');
                this.controller.notificarOperacaoConcluida();
                this.resetFormNovo();
            }, error => {
                this.loading = false;
                this.mensagemService.mostrarMensagem(errorTransform(error));
            });
        }
    }

    /**
     * @description Carrega o registro do banco
     * @param registroId Id do registro a ser carregado
     */
    public carregar(registroId: number): void {
        this.loading = true;
        this.service.carregar(registroId).subscribe(res => {
            this.loading = false;
            this.form.reset(res);
        }, error => {
            this.loading = false;
            this.mensagemService.mostrarMensagem(errorTransform(error));
        });
    }

    /**
     * @description Remove o registro do banco
     * @param registroId Id do registro a ser removido
     */
    public remover(registroId: number): void {
        this.loading = true;
        this.service.remover(registroId).subscribe(() => {
            this.loading = false;
            this.mensagemService.mostrarMensagem('O registro foi excluído com sucesso!');
            this.controller.notificarOperacaoConcluida();
        }, error => {
            this.loading = false;
            this.mensagemService.mostrarMensagem(errorTransform(error));
        });
    }

    /**
     * @description Valida o preenchimento do form
     * @returns True se o form for válido
     * @param markAsDirty Marca o form como dirty antes de validar
     */
    public validarForm(): boolean {
        this.form.markAllAsTouched();
        this.form.updateValueAndValidity();
        return this.form.valid;
    }

    /**
     * @description Reseta o form com o valor de um novo registro
     */
    public resetFormNovo() {
        this.loading = true;
        this.service.novoRegistro.subscribe(res => {
            this.loading = false;
            this.form.reset(res);
        }, error => {
            this.loading = false;
            this.mensagemService.mostrarMensagem(errorTransform(error));
        });
    }

}
