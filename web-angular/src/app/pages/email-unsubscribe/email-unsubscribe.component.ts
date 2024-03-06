import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';
import { errorTransform } from 'src/app/shared/pipes/error-transform';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-email-unsubscribe',
  templateUrl: './email-unsubscribe.component.html',
  styleUrls: ['./email-unsubscribe.component.css'],
  providers: [
    DestinatarioService,
  ]
})
export class EmailUnsubscribeComponent implements OnInit {

  public form: FormGroup;
  public pendingUnsubscribe: boolean = true;

  constructor(
    private formBuilder: FormBuilder,
    private destinatarioService: DestinatarioService,
    private mensagemService: MensagemService,
    public route: ActivatedRoute,
  ) {
    this.buildForm();
  }

  ngOnInit() {
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      reason: [null, [Validators.maxLength(4000)]],
    });
  }

  public get reason() {
    return this.form.get('reason') as AbstractControl<any, any>;
  }

  public onUnsubscribe() {
    this.destinatarioService.unsubscribe({
      uuid: this.getParam('uuid') ?? '',
      reason: this.reason.value,
    }).subscribe({
      next: (response) => {
        this.pendingUnsubscribe = false;
        this.mensagemService.mostrarMensagem(response.message);
      },
      error: (error) => {
        this.pendingUnsubscribe = true;
        this.mensagemService.mostrarMensagem(errorTransform(error));
      }
    });
  }

  public getParam(name: string): string | null {
    return this.route.snapshot.paramMap.get(name);
  }

}
