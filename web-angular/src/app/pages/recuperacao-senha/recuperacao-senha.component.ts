import { ConfirmarSenhaValidator } from 'src/app/shared/validators/confirmar-senha-validator';
import { SenhaValidator } from 'src/app/shared/validators/senha-validator';
import { FormGroup, FormControl, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-recuperacao-senha',
  templateUrl: './recuperacao-senha.component.html',
  styleUrls: ['./recuperacao-senha.component.css']
})
export class RecuperacaoSenhaComponent implements OnInit {

  public form: FormGroup;
  public mensagemErro: String = '';

  constructor(private formBuilder: FormBuilder,) {
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

  }

  public enviarCodigoPorEmail() {
  }

}
