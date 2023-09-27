import { MensagemService } from './../../../../shared/services/mensagem.service';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// material
import { MatDialog } from '@angular/material/dialog';
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
import { GrupoDestinatarioPesquisaDialogComponent } from 'src/app/pages/grupo-destinatarios/components/grupo-destinatario-pesquisa-dialog/grupo-destinatario-pesquisa-dialog.component';
import { GrupoDestinatarioService } from 'src/app/pages/grupo-destinatarios/grupo-destinatario.service';
import { NewsletterEmailGroup } from 'src/app/pages/noticias/models/newsletter-email-group';

@Component({
  selector: 'app-card-noticia-cabecalho',
  templateUrl: 'card-noticia-cabecalho.component.html',
  styleUrls: ['./card-noticia-cabecalho.component.scss'],
  providers: [
    DestinatarioService,
    GrupoDestinatarioService,
  ],
})
export class CardNoticiaCabecalhoComponent extends AdvancedCrudCard<Noticia> implements OnInit {

  @ViewChild('destinatarioInput') public destinatarioInput!: ElementRef<HTMLInputElement>;

  @ViewChild('grupoInput') public grupoInput!: ElementRef<HTMLInputElement>;

  /**
   * @description Classe de controle do auto-complete de destinatários
   */
  public destinatarioAutocomplete!: SysAutocompleteControl;

  /**
   * @description Classe de controle do auto-complete de grupos
   */
  public grupoAutocomplete!: SysAutocompleteControl;

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override formBuilder: FormBuilder,
    public destinatarioService: DestinatarioService,
    public mensagemService: MensagemService,
    public dialog: MatDialog,
    public grupoDestinatarioService: GrupoDestinatarioService,
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

  public get emailGroupsControl() {
    return this.form.get('emailGroups');
  }

  public get emailGroups(): NewsletterEmailGroup[] {
    return this.form.get('emailGroups')?.value || [];
  }

  ngOnInit(): void {
    this.registerControls();
  }

  public override setForm(registro: Noticia) {
    super.setForm(registro);

    if (!registro.emails) {
      this.form.get('emails')?.reset([]);
    }

    if (!registro.emailGroups) {
      this.form.get('emailGroups')?.reset([]);
    }
  }

  private registerControls() {
    this.destinatarioAutocomplete = new SysAutocompleteControl(
      this.destinatarioService.pesquisarTodos.bind(this.destinatarioService),
      this.mensagemService,
      'email'
    );

    this.grupoAutocomplete = new SysAutocompleteControl(
      this.grupoDestinatarioService.pesquisarTodos.bind(this.grupoDestinatarioService),
      this.mensagemService,
      'name'
    );
  }

  criarForm(): FormGroup {
    return this.formBuilder.group({
      description: [null, [Validators.required, MaxLenghtValidator(80)]],
      subject: [null, [Validators.required, MaxLenghtValidator(100)]],
      emails: [null],
      emailGroups: [null],
    })
  }

  /**
   * @description Inclui um destinatário na lista e limpa o input
   */
  public addDestinatario(event: MatAutocompleteSelectedEvent): void {
    if (this.findDestinatarioNoArray(event.option.value.email))
      return;

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

  /**
   * @description Abre a modal de importação do grupo
   */
  public importarGrupo() {
    const dialogRef = this.dialog.open(GrupoDestinatarioPesquisaDialogComponent);
    dialogRef.afterClosed().subscribe((res: Destinatario[]) => {
      if (res) {
        res.forEach(d => {
          let id: number = d['0'];
          let email: string = d['1'];
          if (!this.findDestinatarioNoArray(email))
            this.destinatarios.push({ id: id, email: email });
        });
      }
    })
  }

  private findDestinatarioNoArray(email) {
    return this.destinatarios.find(element => element.email == email);
  }

  /**
   * @description Inclui um grupo na lista e limpa o input
   */
  public addGrupo(event: MatAutocompleteSelectedEvent): void {
    if (this.findGrupoNoArray(event.option.value.name))
      return;

    this.emailGroups.push({ emailGroup: event.option.value });
    this.emailGroupsControl?.reset(this.emailGroups);
    this.grupoInput.nativeElement.value = '';
  }

  /**
   * @description Remove um grupo na lista
   */
  public removeGrupo(index: number): void {
    this.emailGroups.splice(index, 1);
    this.emailGroupsControl?.reset(this.emailGroups);
  }

  /**
   * @description Filtra o auto-complete de grupos
   */
  public filtrarGrupos() {
    this.grupoAutocomplete.filtrar(this.grupoInput.nativeElement.value);
  }

  private findGrupoNoArray(name) {
    return this.emailGroups.find(element => element.emailGroup?.name == name);
  }

}
