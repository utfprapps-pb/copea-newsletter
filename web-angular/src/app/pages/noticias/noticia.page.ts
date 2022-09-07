import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { AdvancedCrudComponent } from 'src/app/shared/crud/advanced-crud-component';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from './models/noticia';
import { NoticiaService } from './noticia.service';

@Component({
    selector: 'app-noticia',
    templateUrl: 'noticia.page.html',
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
     * @description Executa no click do botão publicar
     */
    public onClickPublicar() {
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