
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';
import { getNoticiaEditorConfig } from '../../core/noticia-editor-config';

import Quill from 'quill'
import ImageResize from 'quill-image-resize-module'
Quill.register('modules/imageResize', ImageResize)


@Component({
    selector: 'app-card-noticia-texto',
    templateUrl: 'card-noticia-texto.component.html',
    styleUrls: ['./card-noticia-texto.component.scss']
})
export class CardNoticiaTextoComponent extends AdvancedCrudCard<Noticia> {

    /**
     * @description Armazena a configuração do editor de texto
     */
    public editorConfig: any;

    /**
     * @description Flag que controla o estado 'checado' do slide-toggle
     */
    public slideChecked: boolean;

    public quillModules;

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
        return this.form.get('newsletter')?.value;
    }

    criarForm(): FormGroup {
        return this.formBuilder.group({
            newsletter: [null, Validators.required],
        })
    }

    /**
     * @description Executa no toggleChange do do slide-toggle
     */
    public onSlideChange() {
        this.slideChecked = !this.slideChecked;
    }

}