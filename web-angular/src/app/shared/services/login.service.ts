import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { Observable, Subject, Subscription, interval } from "rxjs";

// environment
import { environment } from "src/environments/environment";

// aplicação
import { LoginRequest } from "../models/login-request";

@Injectable({ providedIn: 'root' })
export abstract class LoginService {

    /**
     * @description Armazena a url base do sistema
     */
    private _url = environment.api;

    /**
     * @description Evento de login
     */
    private _loginEvent: Subject<boolean>;

    /**
     * @description Subscription do interval das chamadas de atualização do token
     */
    private _refreshSubscription?: Subscription;

    constructor(
        private _http: HttpClient,
        private _router: Router,
    ) {
        this._loginEvent = new Subject();
    }

    /**
     * @description Retorna as informações do usuário logado
     */
    public get authInfo(): string | null {
        return localStorage.getItem("access_token");
    }

    /**
     * @description Retorna true se o usuário estiver autenticado
     * // TODO: tipar
     */
    public get isAuthenticated(): boolean {
        return !!localStorage.getItem("access_token");
    }

    /**
     * @description Observable do evento de login
     */
    public get onLoginEvent(): Observable<boolean> {
        return this._loginEvent.asObservable();
    }

    /**
     * @description Loga no sistema
     * @returns Flag que identifica sucesso do login
     * @param request Request de login com email/username e senha
     */
    public login(request: LoginRequest): Observable<boolean> {
        return new Observable(observer => {
            if (!request) {
                observer.error({ message: 'É obrigatório informar um nome de usuário e uma senha para logar no sistema.' });
                observer.complete();
            }

            const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
            const body = 'username=' + request.username + '&password=' + request.password;

            this._http.post<{ access_token: string }>(this._url + 'login', body, { headers: headers }).subscribe(response => {
                this.storeToken(response);

                if (this.isAuthenticated) {
                    this.startRefreshInterval();
                }

                observer.next(this.isAuthenticated);
                observer.complete();
                
                window.location.reload();
            }, error => {
                observer.error(error);
                observer.complete();
            })
        });
    }

    /**
     * @description Armazena/remove o token de acesso
     */
    private storeToken(response?: { access_token: string }) {
        const token = response ? response['access_token'] : null;

        if (token) {
            localStorage.setItem('access_token', token);
        } else {
            localStorage.removeItem('access_token')
        }

        this._loginEvent.next(this.isAuthenticated);
    }

    /**
     * @description Inicia a sequência de chamadas refresh com intervalo de 60 minutos
     */
    private startRefreshInterval() {
        if (this._refreshSubscription && !this._refreshSubscription.closed) {
            this._refreshSubscription.unsubscribe();
        }

        this._refreshSubscription = new Subscription();

        const source = interval(600000);
        const subRefresh = source.subscribe(() => this.refreshToken());

        this._refreshSubscription.add(subRefresh);
    }

    /**
     * @description Aborta as chamadas de refresh token
     */
    private stopRefreshInterval() {
        this._refreshSubscription?.unsubscribe();
    }

    /**
     * @description Atualiza o token de acesso
     */
    private refreshToken(): void {
        this._http.get<{ access_token: string }>(this._url + 'refresh-token').subscribe(response => {
            this.storeToken(response);
        }, error => {
            this.storeToken();
            this.stopRefreshInterval();
        });
    }

    /**
     * @description Desloga do sistema
     */
    public logout() {
        localStorage.removeItem("access_token");
        this._loginEvent.next(false);
        this.stopRefreshInterval();
        this._router.navigateByUrl("").then(res => res);
    }

}