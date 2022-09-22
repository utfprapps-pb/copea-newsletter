import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/shared/services/login.service';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

    constructor(
        private _loginService: LoginService
    ) { }

    ngOnInit() {
    }

    public sair() {
        this._loginService.logout();
    }

}
