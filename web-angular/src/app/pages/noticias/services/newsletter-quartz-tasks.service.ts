import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { CrudService } from 'src/app/shared/crud/crud.service';

import { NewsletterQuartzTasks } from 'src/app/pages/noticias/cards/card-newsletter-schedule/models/newsletter-quartz-tasks';

@Injectable()
export class NewsletterQuartzTasksService extends CrudService<NewsletterQuartzTasks> {

  constructor(public override http: HttpClient) {
    super('/v1/newsletter/quartz-task', http);
  }

  public get novoRegistro(): Observable<NewsletterQuartzTasks> {
    // { newsletter: { description: '', subject: '' }, quartzTask: { createdAt: null, startAt: null, dayRange: null, endAt: null } }
    return of();
  }

  public schedule(newsletterQuartzTasks: NewsletterQuartzTasks): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${this.url}`, newsletterQuartzTasks);
  }

}
