import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';

// aplicação
import { ConfigEmail } from '../models/config-email';

@Injectable()
export class ConfiguracaoEmailService extends CrudService<ConfigEmail> {

    constructor(public override http: HttpClient) {
        super('/v1/email/config', http);
    }

    public get novoRegistro(): Observable<ConfigEmail> {
        return new Observable(observer => {
            this.http.get<ConfigEmail[]>(this.baseUrl + this.url).subscribe(res => {
                observer.next(res[0]);
                observer.complete();
            }, error => {
                observer.error(error);
                observer.complete();
            })
        });
    }

}
