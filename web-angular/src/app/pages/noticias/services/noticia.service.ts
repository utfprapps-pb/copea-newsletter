import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';
import { LastSentEmailNewsletter } from '../models/last-sent-email-newsletter';

// aplicação
import { Noticia } from '../models/noticia';
import { NewsletterSearchRequest } from 'src/app/pages/noticias/models/newsletter-search-request';

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

  public search(newsletterSearchRequest: NewsletterSearchRequest): Observable<Noticia[]> {
    return this.http.post<Noticia[]>(`${this.baseUrl + this.url}/search`, newsletterSearchRequest);
  }

  public getLastSentEmail(newsletterId): Observable<LastSentEmailNewsletter> {
    if (!newsletterId)
      return new Observable<LastSentEmailNewsletter>;
    return this.http.get<LastSentEmailNewsletter>(`${this.baseUrl}/${this.url}/last-sent-email/newsletter/${newsletterId}`);
  }

}
