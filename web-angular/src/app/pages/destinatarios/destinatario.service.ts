import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';

// aplicação
import { Destinatario } from './model/destinatario';
import { EmailSelfRegistration } from 'src/app/pages/email-self-registration/models/email-self-registration';
import { DefaultResponse } from 'src/app/shared/models/default-response';
import { EmailUnsubscribeRequest } from 'src/app/pages/email-unsubscribe/request/email-unsubscribe-request';

@Injectable()
export class DestinatarioService extends CrudService<Destinatario> {

  constructor(public override http: HttpClient) {
    super('/v1/email', http);
  }

  public get novoRegistro(): Observable<Destinatario> {
    return of({ email: '', emailGroupRelations: [], subscribed: 'YES' });
  }

  /**
   * @description Retorna os destinatários do grupo
   */
  public buscarPorGrupo(grupoId: number): Observable<Destinatario[]> {
    const params = new HttpParams().append('groupId', grupoId + '');
    return this.http.get<Destinatario[]>(this.baseUrl + this.url + '/find-by-group', { params: params });
  }

  public exists(id: number, email: string): Observable<any> {
    let params = new HttpParams();
    if (id)
      params = params.append('id', id);
    if (email)
      params = params.append('email', email);
    return this.http.get<any>(`${this.baseUrl}${this.url}/exists`, { params: params });
  }

  public saveSelfEmailRegistration(emailSelfRegistration: EmailSelfRegistration): Observable<DefaultResponse> {
    return this.http.post<DefaultResponse>(`${this.baseUrl}${this.url}/self-registration`, emailSelfRegistration);
  }

  public unsubscribe(emailUnsubscribeRequest: EmailUnsubscribeRequest): Observable<DefaultResponse> {
    return this.http.post<DefaultResponse>(`${this.baseUrl}${this.url}/unsubscribe`, emailUnsubscribeRequest);
  }

}
