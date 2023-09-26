import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { Observable, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { GrupoDestinatarioService } from '../../../grupo-destinatarios/grupo-destinatario.service';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

const CONTENT_COPY_ICON =
  `<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24">
  <path d="M360-240q-33 0-56.5-23.5T280-320v-480q0-33 23.5-56.5T360-880h360q33 0 56.5 23.` +
  `5T800-800v480q0 33-23.5 56.5T720-240H360Zm0-80h360v-480H360v480ZM200-80q-33 0-56.5-23.` +
  `5T120-160v-560h80v560h440v80H200Zm160-240v-480 480Z"/></svg>`;

@Component({
  selector: 'app-card-email-group-edicao',
  templateUrl: './card-email-group-edicao.component.html',
  styleUrls: ['./card-email-group-edicao.component.css'],
})
export class CardEmailGroupEdicaoComponent implements OnInit {

  @Input() public form!: FormGroup;

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
    private iconRegistry: MatIconRegistry,
    private sanitizer: DomSanitizer,
    private mensagemService: MensagemService,
  ) {
    this.addSvgIcons();
  }

  private addSvgIcons() {
    this.iconRegistry.addSvgIconLiteral('content-copy', this.sanitizer.bypassSecurityTrustHtml(CONTENT_COPY_ICON));
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

  public onClickContentCopy() {
    this.mensagemService.mostrarMensagem('Texto copiado!')
  }

  public onGerarLinkClick() {
    this.uuidToSelfRegistrationGenerate();
  }

  private uuidToSelfRegistrationGenerate() {
    if (!this.id.value) {
      return;
    }

    this.grupoDestinatarioService.uuidGenerate(this.id.value).subscribe({
      next: (grupo) => {
        this.uuidToSelfRegistration.setValue(grupo.uuidToSelfRegistration);
      }
    })
  }

  private changeUuidToSelfRegistration(value: string) {
    this.linkEmailSelfRegistration = (value) ? this.mountLinkEmailSelfRegistration(value) : 'Nenhum link gerado';
  }

  private mountLinkEmailSelfRegistration(uuid: string) {
    return `${location.origin}/#/email-self-registration/group/${uuid}`;
  }

  public onRemoveLinkClick() {
    this.removeLink();
  }

  private removeLink() {
    if (!this.id.value) {
      return;
    }

    this.grupoDestinatarioService.uuidRemove(this.id.value).subscribe({
      next: () => this.uuidToSelfRegistration.reset()
    })
  }

}
