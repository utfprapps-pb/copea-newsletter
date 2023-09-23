import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';
import { errorTransform } from 'src/app/shared/pipes/error-transform';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-email-self-registration',
  templateUrl: './email-self-registration.component.html',
  styleUrls: ['./email-self-registration.component.css']
})
export class EmailSelfRegistrationComponent implements OnInit {

  public form: FormGroup;
  public pendingSubscribe: boolean = true;

  constructor(
    public route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private destinatarioService: DestinatarioService,
    private mensagemService: MensagemService,
  ) {
    this.buildForm();
  }

  ngOnInit() {
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
    });
  }

  public get email() {
    return this.form.get('email') as AbstractControl<any, any>;
  }

  public onSubscribe() {
    this.subscribe();
  }

  private subscribe() {
    if (this.form.invalid)
      return;

    this.destinatarioService.saveSelfEmailRegistration({
      email: this.email.value,
      groupUuid: this.getParam('uuid') ?? '',
    }).subscribe({
      next: (response) => {
        this.pendingSubscribe = false;
        this.mensagemService.mostrarMensagem(response.message);
      },
      error: (error) => {
        this.pendingSubscribe = true;
        this.mensagemService.mostrarMensagem(errorTransform(error));
      }
    });
  }

  public getParam(name: string): string | null {
    return this.route.snapshot.paramMap.get(name);
  }

}
