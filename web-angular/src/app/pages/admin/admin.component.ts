import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

// shared
import { LoginService } from 'src/app/shared/services/login.service';

// aplicação
import { ConfiguracaoComponent } from '../configuracao/configuracao.component';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

    constructor(
        private _loginService: LoginService,
        public dialog: MatDialog,
    ) { }

    ngOnInit() {
    }

    public abrirConfiguracoes() {
        this.dialog.open(ConfiguracaoComponent);
    }

    public sair() {
        this._loginService.logout();
    }

}
