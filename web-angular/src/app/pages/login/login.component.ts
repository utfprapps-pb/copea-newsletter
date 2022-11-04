import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
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

    constructor(
        private auth: AuthService,
        private _loginService: LoginService,
        private router: Router
    ) { }

    public form: FormGroup = new FormGroup({
        username: new FormControl(),
        password: new FormControl(),
    });

    ngOnInit() {
        this._loginService.logout();
    }

    public login() {
        this._loginService.login(this.form.value).subscribe((resposta: any) => {
            // this.auth.token = resposta.data.token;
            this.mensagemErro = '';
            this.router.navigateByUrl('admin');
        }, error => {
            console.error(error);
            this.mensagemErro = error;
        });
    }


}
