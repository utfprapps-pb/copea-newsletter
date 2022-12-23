import { MensagemService } from './../../shared/services/mensagem.service';
import { Router } from '@angular/router';
import { RecuperacaoSenhaDTO } from './../../models/recuperacao-senha-dto';
import { RecuperacaoSenhaService } from './recuperacao-senha.service';
import { ConfirmarSenhaValidator } from 'src/app/shared/validators/confirmar-senha-validator';
import { SenhaValidator } from 'src/app/shared/validators/senha-validator';
import { FormGroup, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-recuperacao-senha',
  templateUrl: './recuperacao-senha.component.html',
  styleUrls: ['./recuperacao-senha.component.css']
})
export class RecuperacaoSenhaComponent implements OnInit {

  public form: FormGroup;
  public mensagemErro: String = '';

  constructor(
    private formBuilder: FormBuilder,
    private recuperacaoSenhaService: RecuperacaoSenhaService,
    private router: Router,
    private mensagemService: MensagemService,
    ) {
    this.buildForm();
  }

  ngOnInit() {
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      username: [null, Validators.required],
      codigo: [null, Validators.required],
      password: [null, Validators.compose([
        Validators.required, SenhaValidator
      ])],
      confirmarSenha: [null, Validators.required],
    });
    this.confirmarSenha.setValidators(ConfirmarSenhaValidator.validar(this.senha));
  }

  public get username() {
    return this.form.get('username') as AbstractControl<any, any>;
  }

  public get codigo() {
    return this.form.get('codigo') as AbstractControl<any, any>;
  }

  public get senha() {
    return this.form.get('password') as AbstractControl<any, any>;
  }

  public get confirmarSenha() {
    return this.form.get('confirmarSenha') as AbstractControl<any, any>;
  }

  public trocarSenha() {
    let recuperacaoSenhaDTO: RecuperacaoSenhaDTO;
    recuperacaoSenhaDTO = {
      username: this.username.value,
      code: this.codigo.value,
      newPassword: this.confirmarSenha.value,
    };
    this.recuperacaoSenhaService.trocarSenha(recuperacaoSenhaDTO).subscribe(
      {
        next: (value: any) => {
          this.mensagemService.mostrarMensagem(value.message);
          this.router.navigateByUrl('login')
        },
        error: (err) => {
          console.log(err);
          this.mensagemService.mostrarMensagem(err.error.message);
        },
      })
  }

  public enviarCodigoPorEmail() {
    if (!this.username.value) {
      this.username.setErrors({ invalido: 'Informe o usuário para receber o código por e-mail.' })
      return;
    }

    this.recuperacaoSenhaService.enviarCodigoPorEmail(this.username.value).subscribe(
      {
        next: (value: any) => {
          this.mensagemService.mostrarMensagem(value.message);
        },
        error: (err) => {
          console.log(err);
          this.mensagemService.mostrarMensagem(err.error.message);
        },
      })
  }

}
