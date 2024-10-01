import { Component, OnInit } from '@angular/core';
import { FormGroup, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

// shared
import { LoginService } from 'src/app/shared/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public mensagemErro: String = '';
  public form: FormGroup;

  constructor(
    private auth: AuthService,
    private _loginService: LoginService,
    private router: Router,
    private formBuilder: FormBuilder,
  ) {
    this.buildForm();
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      username: [null, Validators.required],
      password: [null, Validators.required],
    });
  }

  ngOnInit() {
    this._loginService.logout();
  }

  public get username() {
    return this.form.get('username') as AbstractControl<any, any>;
  }

  public get password() {
    return this.form.get('password') as AbstractControl<any, any>;
  }

  public login() {
    this._loginService.login(this.form.value).subscribe((resposta: any) => {
      // this.auth.token = resposta.data.token;
      this.mensagemErro = '';
      this.router.navigateByUrl('admin/bem-vindo');
    }, error => {
      console.error(error);
      this.mensagemErro = error;
    });
  }

}
