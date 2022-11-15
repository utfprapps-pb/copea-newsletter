import { LabelValue } from './../../shared/models/label-value';
import { UserService } from './../../services/user.service';
import { CadastroService } from './cadastro.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth.service';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// shared
import { ConfirmarSenhaValidator } from 'src/app/shared/validators/confirmar-senha-validator';
import { SenhaValidator } from 'src/app/shared/validators/senha-validator';
import { endWith } from 'rxjs';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css']
})
export class CadastroComponent implements OnInit {

  public form: FormGroup;
  public loading: Boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private auth: AuthService,
    public snackBar: MatSnackBar,
    private userService: UserService,
  ) {
    this.form = this.formBuilder.group({
      fullname: [null, Validators.required],
      username: [null, Validators.required],
      email: [null, Validators.compose([
        Validators.required,
        Validators.email
      ])],
      password: [null, Validators.compose([
        Validators.required, SenhaValidator
      ])],
      confirmarSenha: [null]
    });

    this.confirmarSenha.setValidators(ConfirmarSenhaValidator.validar(this.senha));
  }

  ngOnInit() {
  }


  public get senha() {
    return this.form.get('password') as AbstractControl<any, any>;
  }

  public get nome() {
    return this.form.get('fullname') as AbstractControl<any, any>;
  }

  public get email() {
    return this.form.get('email') as AbstractControl<any, any>;
  }

  public get username() {
    return this.form.get('username') as AbstractControl<any, any>;
  }

  public get confirmarSenha() {
    return this.form.get('confirmarSenha') as AbstractControl<any, any>;
  }

  public cadastrar() {
    const usuario = this.form.value;
    delete usuario.corfirmarSenha;
    this.loading = true;
    this.auth.cadastrar(usuario).subscribe(() => {
      this.loading = false;
      this.router.navigateByUrl('login');
    }, (error) => {
      this.loading = false;
      alert('Ocorreu algum erro ao cadastrar');
    });
  }

  public changeUsername() {
    this.validarUsuarioExistente(this.username, "usuário");
  }

  public changeEmail() {
    this.validarUsuarioExistente(this.email, "email");
  }

  private validarUsuarioExistente(campo: AbstractControl<any, any>, nome: string) {
    this.userService.findUserByUsernameOrEmail(campo.value, campo.value).subscribe({
      next: (response: any) => {
        if (response.exists) {
          campo.setErrors({ invalido: `Este ${nome} já existe, informe outro.` });
          campo.markAllAsTouched();
        }
      },
      error: (error) => console.log(`erro ao validar ${nome}: ${error}`),
    });
  }

}
