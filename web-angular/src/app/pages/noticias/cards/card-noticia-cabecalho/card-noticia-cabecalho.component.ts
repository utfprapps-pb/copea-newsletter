
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { MaxLenghtValidator } from 'src/app/shared/validators/max-length-validator';
import { SysAutocompleteControl } from 'src/app/shared/components/autocomplete/sys-autocomplete';

// pages
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';

@Component({
    selector: 'app-card-noticia-cabecalho',
    templateUrl: 'card-noticia-cabecalho.component.html',
    providers: [DestinatarioService],
})
export class CardNoticiaCabecalhoComponent extends AdvancedCrudCard<Noticia> implements OnInit {

    @ViewChild('chipInput') public palavrasChaveInput!: ElementRef<HTMLInputElement>;
    @ViewChild('categoriaInput') public categoriaInput!: ElementRef<HTMLInputElement>;

    /**
     * @description Classe de controle do auto-complete de categoria
     */
    public destinatarioAutocomplete!: SysAutocompleteControl;

    constructor(
        public override crudController: AdvancedCrudController<Noticia>,
        public override formBuilder: FormBuilder,
        public destinatarioService: DestinatarioService,
        public snackBar: MatSnackBar,
    ) {
        super(crudController, formBuilder);
    }

    public get tituloControl() {
        return this.form.get('descricao');
    }

    public get destinatariosControl() {
        return this.form.get('destinatarios');
    }

    ngOnInit(): void {
        this.registerControls();
    }

    private registerControls() {
        this.destinatarioAutocomplete = new SysAutocompleteControl(
            this.destinatarioService.pesquisarTodos.bind(this.destinatarioService), this.snackBar
        );
    }

    criarForm(): FormGroup {
        return this.formBuilder.group({
            descricao: [null, [Validators.required, MaxLenghtValidator(80)]],
            destinatarios: [null]
        })
    }

    /**
     * @description Filtra o auto-complete de categoria
     */
    public filtrarCategoria() {
        this.destinatarioAutocomplete.filtrar(this.categoriaInput.nativeElement.value);
    }

}
