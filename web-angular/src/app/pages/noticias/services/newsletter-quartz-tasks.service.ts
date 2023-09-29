import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { CrudService } from 'src/app/shared/crud/crud.service';

import { NewsletterQuartzTasks } from 'src/app/pages/noticias/cards/card-newsletter-schedule/models/newsletter-quartz-tasks';
import { QuartzTasks } from 'src/app/pages/noticias/cards/card-newsletter-schedule/models/quartz-tasks';

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
    return this.http.post<any>(`${this.baseUrl}${this.url}`, newsletterQuartzTasks);
  }

  public getActiveSchedules(newsletterId: number): Observable<Array<QuartzTasks>> {
    let params = new HttpParams();
    if (newsletterId)
      params.append('newsletter-id', newsletterId);
    return this.http.get<Array<QuartzTasks>>(`${this.baseUrl}${this.url}/active-schedules`, { params: params });
  }

}
