import { MensagemService } from './../services/mensagem.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';

// shared
import { LoginService } from 'src/app/shared/services/login.service';

@Injectable()
export class RoleGuard implements CanActivate {

    constructor(
        public loginService: LoginService,
        private mensagemService: MensagemService,
        public router: Router,
    ) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {
        return new Observable<boolean>(observer => {
            if (this.loginService.isAuthenticated) {
                observer.next(true);
                observer.complete();
            } else {
                this.alertNoPermission();
                observer.next(false);
                observer.complete();
                this.router.navigateByUrl('login');
            }
        });
    }

    private alertNoPermission(): void {
      this.mensagemService.mostrarMensagem('Você não tem permissão para acessar esse recurso!');
    }

}
