import { MensagemService } from './../../../../shared/services/mensagem.service';

import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';

// material
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatDialog } from '@angular/material/dialog';

// shared
import { SysAutocompleteControl } from 'src/app/shared/components/autocomplete/sys-autocomplete';

// aplicação
import { GrupoDestinatarioDialogComponent } from 'src/app/pages/grupo-destinatarios/components/grupo-destinatario-dialog/grupo-destinatario-dialog.component';
import { GrupoDestinatarioService } from 'src/app/pages/grupo-destinatarios/grupo-destinatario.service';
import { GrupoDestinatario } from '../../../grupo-destinatarios/model/grupo-destinatario';

@Component({
  selector: 'app-card-destinatario-edicao',
  templateUrl: 'card-destinatario-edicao.component.html'
})
export class CardDestinatarioEdicaoComponent implements OnInit {

  @ViewChild('grupoInput') public grupoInput!: ElementRef<HTMLInputElement>;
  @ViewChild('emailInput') public emailInput!: ElementRef<HTMLInputElement>;

  /**
   * @description Recebe o FormGroup do card
   */
  @Input() public form!: FormGroup;

  /**
   * @description Classe de controle do auto-complete de destinatários
   */
  public grupoAutocomplete!: SysAutocompleteControl;

  /**
   * @description Evento de persistir o registro em edição
   */
  private _persistirEdicaoEvent: Subject<void> = new Subject();

  /**
   * @description Evento de remoção o registro em edição
   */
  private _removerRegistroEvent: Subject<number> = new Subject();

  private _resetFormNovo: Subject<void> = new Subject();

  private _adicionandoGrupo: boolean = false;

  constructor(
    private _grupoService: GrupoDestinatarioService,
    private _dialog: MatDialog,
    public mensagemService: MensagemService,
  ) { }

  ngOnInit(): void {
    this.registerControls();
  }

  public get email(): AbstractControl {
    return this.form.get('email')!;
  }

  public get gruposControl() {
    return this.form.get('groups');
  }

  public get grupos(): GrupoDestinatario[] {
    return this.form.get('groups')?.value || [];
  }

  public get subscribed(): AbstractControl {
    return this.form.get('subscribed')!;
  }

  public get lastEmailUnsubscribedDate(): AbstractControl {
    return this.form.get('lastEmailUnsubscribedDate')!;
  }

  /**
   * @description Lança o evento de persistência da edição
   */
  public persistirEdicao(): void {
    if (this.adicionandoGrupo) {
      this.adicionandoGrupo = false;
      return;
    }
    this._persistirEdicaoEvent.next();
    this.emailInput.nativeElement.focus();
  }

  /**
   * @description Lança o evento de remoção do registro
   */
  public removerRegistro(): void {
    if (this.form.get('id')?.value && confirm('Você tem certeza que deseja remover o destinatário? Essa ação não poderá ser desfeita.')) {
      this._removerRegistroEvent.next(this.form.get('id')?.value);
    }
  }

  public get persistirEdicaoEvent(): Observable<void> {
    return this._persistirEdicaoEvent.asObservable();
  }

  public get removerRegistroEvent(): Observable<number> {
    return this._removerRegistroEvent.asObservable();
  }

  public get resetFormNovoEvent(): Observable<void> {
    return this._resetFormNovo.asObservable();
  }

  private registerControls() {
    this.grupoAutocomplete = new SysAutocompleteControl(
      this._grupoService.pesquisarTodos.bind(this._grupoService),
      this.mensagemService,
      'name'
    );
  }

  /**
   * @description Inclui um grupo na lista e limpa o input
   */
  public addGrupo(event: MatAutocompleteSelectedEvent): void {
    if (this.findNameGrupoNoArray(event.option.value.name))
      return;

    this.grupos.push(event.option.value);
    this.gruposControl?.reset(this.grupos);
    this.grupoInput.nativeElement.value = '';

    this.adicionandoGrupo = true;
  }

  private findNameGrupoNoArray(name) {
    return this.grupos.find(element => element.name == name);
  }

  public set adicionandoGrupo(value: boolean) {
    this._adicionandoGrupo = value;
  }

  public get adicionandoGrupo(): boolean {
    return this._adicionandoGrupo;
  }

  /**
   * @description Remove um grupo na lista
   */
  public removeGrupo(index: number): void {
    this.grupos.splice(index, 1);
    this.gruposControl?.reset(this.grupos);
  }

  public atualizarVizualizacaoNomeGrupoNoCampo(grupo: GrupoDestinatario): void {
    this.grupos.forEach((value: GrupoDestinatario) => {
      if (value.id == grupo.id)
        value.name = grupo.name;
    });
    this.gruposControl?.reset(this.grupos);
  }

  /**
   * @description Filtra o auto-complete de grupos
   */
  public filtrarGrupos() {
    this.grupoAutocomplete.filtrar(this.grupoInput.nativeElement.value);
  }

  /**
   * @description Abre o cadastro do grupo
   */
  public abrirModalCadastroGrupo(registro?: GrupoDestinatario, indexGrupo?: number): void {
    this._dialog.open(GrupoDestinatarioDialogComponent, { data: { registro: registro, indexGrupo: indexGrupo, parentComponent: this } });
  }

  public changeEventCheckSubscribed(event) {
    this.subscribed.setValue(event.checked ? "YES" : "NO");
  }

  public resetFormNovo() {
    this._resetFormNovo.next();
  }

}
