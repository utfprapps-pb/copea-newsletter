import { MatDrawer, MatDrawerContainer } from '@angular/material/sidenav';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

// shared
import { LoginService } from 'src/app/shared/services/login.service';

// aplicação
import { ConfiguracaoComponent } from '../configuracao/configuracao.component';
import { DrawerService } from './drawer.service';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

    @ViewChild('drawer', {static: true}) public drawer: MatDrawer;
    @ViewChild('drawerContainer', {static: true}) public drawerContainer: MatDrawerContainer;


    constructor(
        private _loginService: LoginService,
        public dialog: MatDialog,
        private sidenavService: DrawerService
    ) {

     }

    ngOnInit() {
      this.sidenavService.setDrawer(this.drawer);
      console.log(this.drawerContainer);
      this.sidenavService.matDrawerContainer = this.drawerContainer;
    }

    public abrirConfiguracoes() {
        this.dialog.open(ConfiguracaoComponent);
    }

    public sair() {
        this._loginService.logout();
    }

    onOpenedChange(e: boolean) {
      console.log('onOpenedChange' + e);
    }

}
