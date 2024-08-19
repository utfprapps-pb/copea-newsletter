import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { Observable, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { GrupoDestinatarioService } from '../../../grupo-destinatarios/grupo-destinatario.service';

@Component({
  selector: 'app-card-email-group-edicao',
  templateUrl: './card-email-group-edicao.component.html',
  styleUrls: ['./card-email-group-edicao.component.css'],
})
export class CardEmailGroupEdicaoComponent implements OnInit {

  @Input() public form!: FormGroup;

  @Output('carregarRegistrosPesquisa') public carregarRegistrosPesquisaEvent = new EventEmitter();

  @ViewChild('nameInput') public nameInput!: ElementRef<HTMLInputElement>;

  private _resetFormNovo: Subject<void> = new Subject();

  /**
 * @description Evento de persistir o registro em edição
 */
  private _persistirEdicaoEvent: Subject<void> = new Subject();

  /**
   * @description Evento de remoção o registro em edição
   */
  private _removerRegistroEvent: Subject<number> = new Subject();

  public linkEmailSelfRegistration = '';

  constructor(
    private grupoDestinatarioService: GrupoDestinatarioService,
  ) {
  }

  ngOnInit() {
    this.implementEvents();
  }

  private implementEvents() {
    this.name.valueChanges.pipe(debounceTime(500)).subscribe(this.changeName.bind(this));
    this.uuidToSelfRegistration.valueChanges.pipe(distinctUntilChanged()).subscribe(this.changeUuidToSelfRegistration.bind(this));
  }

  public get resetFormNovoEvent(): Observable<void> {
    return this._resetFormNovo.asObservable();
  }

  public get removerRegistroEvent(): Observable<number> {
    return this._removerRegistroEvent.asObservable();
  }

  public resetFormNovo() {
    this._resetFormNovo.next();
  }

  /**
   * @description Lança o evento de persistência da edição
   */
  public persistirEdicao(): void {
    this._persistirEdicaoEvent.next();
    this.nameInput.nativeElement.focus();
  }

  public get persistirEdicaoEvent(): Observable<void> {
    return this._persistirEdicaoEvent.asObservable();
  }

  /**
   * @description Lança o evento de remoção do registro
   */
  public removerRegistro(): void {
    if (this.form.get('id')?.value && confirm('Você tem certeza que deseja remover o grupo? Essa ação não poderá ser desfeita.')) {
      this._removerRegistroEvent.next(this.form.get('id')?.value);
    }
  }

  public get id(): AbstractControl<any, any> {
    return this.form.get('id') as AbstractControl<any, any>;
  }

  public get name(): AbstractControl<any, any> {
    return this.form.get('name') as AbstractControl<any, any>;
  }

  public get uuidToSelfRegistration(): AbstractControl<any, any> {
    return this.form.get('uuidToSelfRegistration') as AbstractControl<any, any>;
  }

  public changeName(name: string) {
    this.validarGrupoExistente(this.name, 'grupo')
  }

  private validarGrupoExistente(campo: AbstractControl<any, any>, nome: string) {
    this.grupoDestinatarioService.exists(this.id.value, campo.value).subscribe({
      next: (response: any) => {
        if (response.exists) {
          campo.setErrors({ invalido: `Este ${nome} já existe, informe outro.` });
          campo.markAllAsTouched();
        }
      },
      error: (error) => console.log(`erro ao validar ${nome}: ${error}`),
    });
  }

  private changeUuidToSelfRegistration(value: string) {
    this.linkEmailSelfRegistration = (value) ? this.mountLinkEmailSelfRegistration(value) : 'Nenhum link gerado';
  }

  private mountLinkEmailSelfRegistration(uuid: string) {
    return "${URL_TO_SELF_REGISTRATION}/" + uuid;
  }

  public onNewUuid(uuid: string) {
    this.uuidToSelfRegistration.setValue(uuid);
    this.carregarRegistrosPesquisaEvent.emit();
  }

  public onRemoveUuid() {
    this.uuidToSelfRegistration.reset();
    this.carregarRegistrosPesquisaEvent.emit();
  }

}
