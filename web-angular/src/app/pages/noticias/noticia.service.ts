import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';

// aplicação
import { Noticia } from './models/noticia';

@Injectable()
export class NoticiaService extends CrudService<Noticia> {

  constructor(public override http: HttpClient) {
    super('/v1/newsletter', http);
  }

  public get novoRegistro(): Observable<Noticia> {
    return of({ description: '', subject: '' });
  }

  public enviarNoticia(id: number): any {
    return this.http.get<any>(this.baseUrl + '/v1/newsletter/' + id + '/send-email');
  }

  public search(filtro): Observable<Noticia[]> {
    let params = new HttpParams()
      .append('newslettersSent', (filtro.filtros.indexOf('ENVIADAS') > -1))
      .append('newslettersNotSent', (filtro.filtros.indexOf('NAO_ENVIADAS') > -1))
      .append('newsletterTemplate', (filtro.filtros.indexOf('MODELO') > -1))
      .append('description', filtro.descricao);
    return this.http.get<Noticia[]>(`${this.baseUrl + this.url}/search`, {params: params});
  }

}
