
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// material
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { MaxLenghtValidator } from 'src/app/shared/validators/max-length-validator';
import { SysAutocompleteControl } from 'src/app/shared/components/autocomplete/sys-autocomplete';

// pages
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';
import { Destinatario } from 'src/app/pages/destinatarios/model/destinatario';

// aplicação
import { Noticia } from '../../models/noticia';

@Component({
    selector: 'app-card-noticia-cabecalho',
    templateUrl: 'card-noticia-cabecalho.component.html',
    providers: [DestinatarioService],
})
export class CardNoticiaCabecalhoComponent extends AdvancedCrudCard<Noticia> implements OnInit {

    @ViewChild('destinatarioInput') public destinatarioInput!: ElementRef<HTMLInputElement>;

    /**
     * @description Classe de controle do auto-complete de destinatários
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
        return this.form.get('description');
    }

    public get subjectControl() {
        return this.form.get('subject');
    }

    public get destinatariosControl() {
        return this.form.get('emails');
    }
    
    public get destinatarios(): Destinatario[] {
        return this.form.get('emails')?.value || [];
    }

    ngOnInit(): void {
        this.registerControls();
    }

    private registerControls() {
        this.destinatarioAutocomplete = new SysAutocompleteControl(
            this.destinatarioService.pesquisarTodos.bind(this.destinatarioService), 
            this.snackBar,
            'email'
        );
    }

    criarForm(): FormGroup {
        return this.formBuilder.group({
            description: [null, [Validators.required, MaxLenghtValidator(80)]],
            subject: [null, [Validators.required, MaxLenghtValidator(100)]],
            emails: [null]
        })
    }

    /**
     * @description Inclui um destinatário na lista e limpa o input
     */
    public addDestinatario(event: MatAutocompleteSelectedEvent): void {
        this.destinatarios.push(event.option.value);
        this.destinatariosControl?.reset(this.destinatarios);
        this.destinatarioInput.nativeElement.value = '';
    }

    /**
     * @description Remove um destinatário na lista
     */
    public removeDestinatario(index: number): void {
        this.destinatarios.splice(index, 1);
        this.destinatariosControl?.reset(this.destinatarios);
    }

    /**
     * @description Filtra o auto-complete de destinatários
     */
    public filtrarDestinatarios() {
        this.destinatarioAutocomplete.filtrar(this.destinatarioInput.nativeElement.value);
    }

}