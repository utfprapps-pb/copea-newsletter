import { Clipboard } from '@angular/cdk/clipboard';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { GrupoDestinatarioService } from 'src/app/pages/grupo-destinatarios/grupo-destinatario.service';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

const CONTENT_COPY_ICON =
  `<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24">
  <path d="M360-240q-33 0-56.5-23.5T280-320v-480q0-33 23.5-56.5T360-880h360q33 0 56.5 23.` +
  `5T800-800v480q0 33-23.5 56.5T720-240H360Zm0-80h360v-480H360v480ZM200-80q-33 0-56.5-23.` +
  `5T120-160v-560h80v560h440v80H200Zm160-240v-480 480Z"/></svg>`;

@Component({
  selector: 'app-uuid-self-registration-buttons-handle',
  templateUrl: './uuid-self-registration-buttons-handle.component.html',
  styleUrls: ['./uuid-self-registration-buttons-handle.component.css']
})
export class UuidSelfRegistrationButtonsHandleComponent implements OnInit {

  @Input() public emailGroupId: number;
  @Input() public currentUuid: string;

  @Output('newUuid') public newUuidEvent = new EventEmitter<string>();
  @Output('removeUuid') public removeUuidEvent = new EventEmitter();

  constructor(
    private mensagemService: MensagemService,
    private grupoDestinatarioService: GrupoDestinatarioService,
    private iconRegistry: MatIconRegistry,
    private sanitizer: DomSanitizer,
    private clipboard: Clipboard,
  ) {
    this.addSvgIcons();
  }

  private addSvgIcons() {
    this.iconRegistry.addSvgIconLiteral('content-copy', this.sanitizer.bypassSecurityTrustHtml(CONTENT_COPY_ICON));
  }

  ngOnInit() {
  }

  public onClickContentCopy() {
    this.copyLinkToClipboard(this.currentUuid);
    this.mensagemService.mostrarMensagem('Texto copiado!');
  }

  private clipboardCopy(text: string) {
    this.clipboard.copy(text);
  }

  public onGerarLinkClick() {
    this.uuidToSelfRegistrationGenerate();
  }

  private uuidToSelfRegistrationGenerate() {
    if (!this.emailGroupId) {
      return;
    }

    this.grupoDestinatarioService.uuidGenerate(this.emailGroupId).subscribe({
      next: (grupo) => {
        this.newUuidEvent.emit(grupo.uuidToSelfRegistration);
        this.copyLinkToClipboard(grupo.uuidToSelfRegistration ?? '');
        this.mensagemService.mostrarMensagem('Link gerado com sucesso e copiado para a sua área de transferência.');
      }
    })
  }

  private copyLinkToClipboard(uuid: string) {
    if (uuid)
      this.clipboardCopy("${URL_TO_SELF_REGISTRATION}/" + uuid);
  }

  public onRemoveLinkClick() {
    this.removeLink();
  }

  private removeLink() {
    if (!this.emailGroupId) {
      return;
    }

    this.grupoDestinatarioService.uuidRemove(this.emailGroupId).subscribe({
      next: () => this.removeUuidEvent.emit()
    })
  }

}
