
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// editor
import { AngularEditorConfig } from '@kolkov/angular-editor';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';
import { getNoticiaEditorConfig } from '../../core/noticia-editor-config';

@Component({
    selector: 'app-card-noticia-texto',
    templateUrl: 'card-noticia-texto.component.html',
    styleUrls: ['./card-noticia-texto.component.scss']
})
export class CardNoticiaTextoComponent extends AdvancedCrudCard<Noticia> {

    /**
     * @description Armazena a configuração do editor de texto
     */
    public editorConfig: AngularEditorConfig;

    /**
     * @description Flag que controla o estado 'checado' do slide-toggle
     */
    public slideChecked: boolean;

    // TODO: Criar componente próprio para escrever texto (se der tempo)
    constructor(
        public override crudController: AdvancedCrudController<Noticia>,
        public override formBuilder: FormBuilder,
    ) {
        super(crudController, formBuilder);

        // inicializa as variáveis usadas no layout
        this.editorConfig = getNoticiaEditorConfig();
        this.slideChecked = false;
    }

    public get texto(): string {
        return this.form.get('texto')?.value;
    }

    criarForm(): FormGroup {
        return this.formBuilder.group({
            texto: [null, Validators.required],
        })
    }

    /**
     * @description Executa no toggleChange do do slide-toggle
     */
    public onSlideChange() {
        this.slideChecked = !this.slideChecked;
    }

}