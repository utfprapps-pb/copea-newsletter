import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';

// aplicação
import { GrupoDestinatario } from './model/grupo-destinatario';

@Injectable()
export class GrupoDestinatarioService extends CrudService<GrupoDestinatario> {

  constructor(public override http: HttpClient) {
    super('/v1/email-group', http);
  }

  public get novoRegistro(): Observable<GrupoDestinatario> {
    return of({ name: '' });
  }

  public exists(id: number, name: string): Observable<any> {
    let params = new HttpParams();
    if (id)
      params = params.append('id', id);
    if (name)
      params = params.append('name', name);
    return this.http.get<any>(`${this.baseUrl}/${this.url}/exists`, { params: params });
  }

}
