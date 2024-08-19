import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigApplication } from 'src/app/pages/configuracao/components/aplicacao/models/config-application';
import { CrudService } from 'src/app/shared/crud/crud.service';

@Injectable()
export class ConfiguracaoAplicacaoService extends CrudService<ConfigApplication> {

  constructor(public override http: HttpClient) {
    super('/application/config', http);
  }

  public get novoRegistro(): Observable<ConfigApplication> {
    return new Observable(observer => {
      this.http.get<ConfigApplication[]>(this.baseUrl + this.url).subscribe({
        next: (res) => {
          observer.next(res[0]);
          observer.complete();
        },
        error: (error) => {
          observer.error(error);
          observer.complete();
        }
      })
    });
  }

}
