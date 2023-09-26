import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { QuartzTasks } from "src/app/pages/noticias/cards/card-newsletter-schedule/models/quartz-tasks";
import { CrudService } from "src/app/shared/crud/crud.service";

@Injectable()
export class QuartzTasksService extends CrudService<QuartzTasks> {

  constructor(public override http: HttpClient) {
    super('/v1/quartz-task', http);
  }

  public get novoRegistro(): Observable<QuartzTasks> {
    return of();
  }

  public cancel(quartzTasksId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${this.url}/cancel/${quartzTasksId}`);
  }

}
