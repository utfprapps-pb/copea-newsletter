import { AfterViewInit, Component, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { BasicCrudComponent } from 'src/app/shared/crud/basic-crud-component';
import { CrudController } from 'src/app/shared/crud/crud.controller';

// aplicação
import { Destinatario } from './model/destinatario';
import { DestinatarioService } from './destinatario.service';
import { CardDestinatarioEdicaoComponent } from './cards/card-destinatario-edicao/card-destinatario-edicao.component';
import { CardDestinatarioPesquisaComponent } from './cards/card-destinatario-pesquisa/card-destinatario-pesquisa.component';

@Component({
    selector: 'app-Destinatario',
    templateUrl: 'Destinatario.page.html',
    providers: [
        CrudController,
        DestinatarioService,
    ]
})
export class DestinatarioComponent extends BasicCrudComponent<Destinatario> implements AfterViewInit, OnDestroy {

    @ViewChild(CardDestinatarioEdicaoComponent)
    private cardEdicao!: CardDestinatarioEdicaoComponent;

    @ViewChild(CardDestinatarioPesquisaComponent)
    private cardPesquisa!: CardDestinatarioPesquisaComponent;

    /**
     * @description Armazena as incrições de eventos do componente
     */
    private subscription: Subscription;

    constructor(
        public override controller: CrudController,
        public override service: DestinatarioService,
        public override formBuilder: FormBuilder,
        public override snackBar: MatSnackBar, 
        public override route: ActivatedRoute,
    ) { 
        super(controller, formBuilder, service, snackBar, route);
        this.subscription = new Subscription();
    }

    ngAfterViewInit(): void {
        super.ngOnInit();
        this.implementEvents();
    }

    private implementEvents() {
        // events do card de edição
        this.subscription.add(this.cardEdicao.persistirEdicaoEvent.subscribe(() => super.persistirAlteracoes(this.form.get('id')?.value != null)));
        this.subscription.add(this.cardEdicao.removerRegistroEvent.subscribe(this.removerRegistro.bind(this)));
        // events do card de pesquisa
        this.subscription.add(this.cardPesquisa.editarRegistroEvent.subscribe(super.carregar.bind(this)));
        this.subscription.add(this.cardPesquisa.removerRegistroEvent.subscribe(this.removerRegistro.bind(this)));
    }

    public criarForm(): FormGroup {
        return this.formBuilder.group({
            id: [null],
            email: [null, Validators.required]
        })
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

}