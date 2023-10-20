import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { Observable, Subject, Subscription, interval } from "rxjs";

// environment
import { environment } from "src/environments/environment";

// shared
import { errorTransform } from "src/app/shared/pipes/error-transform";

// aplicação
import { LoginRequest } from "../models/login-request";
import { LoginCadastroRequest } from "../models/login-cadastro-request";
import { TokenResponse } from "src/app/shared/models/token-response";

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
    return localStorage.getItem("token");
  }

  /**
   * @description Retorna true se o usuário estiver autenticado
   * // TODO: tipar
   */
  public get isAuthenticated(): boolean {
    return !!localStorage.getItem("token");
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

      const httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      }

      this._http.post<TokenResponse>(this._url + '/login', request, httpOptions).subscribe({
        next: (response) => {
          this.storeToken(response);

          // TODO: necessário implementar um fluxo de refresh token ao invés de dar refresh no access token
          // if (this.isAuthenticated) {
          //   const fiveMinutes = 300000;
          //   this.startRefreshInterval((response.durationMillisecs - fiveMinutes));
          // }

          observer.next(this.isAuthenticated);
          observer.complete();
        },
        error: (error) => {
          if (error.error && error.error.status == 401)
            observer.error(error.error.message);
          else
            observer.error(errorTransform(error));

          observer.complete();
        }
      });
    });
  }

  public cadastrar(request: LoginCadastroRequest): Observable<boolean> {
    return new Observable(observer => {
      if (!request) {
        observer.error({ message: 'É obrigatório informar o Nome completo, email, usuário e senha para se cadastrar no sistema.' });
        observer.complete();
      }

      const httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      }

      this._http.post(this._url + '/user', request, httpOptions).subscribe({
        next: () => {
          observer.next();
          observer.complete();
        },
        error: (error) => {
          observer.error(errorTransform(error));
          observer.complete();
        },
      });

    });
  }

  /**
   * @description Armazena/remove o token de acesso
   */
  private storeToken(response?: TokenResponse) {
    const token = response ? response['token'] : null;

    if (token) {
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token')
    }

    this._loginEvent.next(this.isAuthenticated);
  }

  /**
   * @description Inicia a sequência de chamadas refresh com intervalo de 60 minutos
   */
  private startRefreshInterval(intervalTime: number) {
    if (this._refreshSubscription && !this._refreshSubscription.closed) {
      this._refreshSubscription.unsubscribe();
    }

    this._refreshSubscription = new Subscription();

    const source = interval((intervalTime));
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
    this._http.get<TokenResponse>(this._url + '/login/token/refresh').subscribe({
      next: (response) => {
        this.storeToken(response);
      },
      error: (error) => {
        this.storeToken();
        this.stopRefreshInterval();
      },
    });
  }

  /**
   * @description Desloga do sistema
   */
  public logout() {
    localStorage.removeItem('token');
    this._loginEvent.next(false);
    this.stopRefreshInterval();
    this._router.navigateByUrl('').then(res => res);
  }

}
