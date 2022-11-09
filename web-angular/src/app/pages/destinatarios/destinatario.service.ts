import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// shared
import { CrudService } from 'src/app/shared/crud/crud.service';

// aplicação
import { Destinatario } from './model/destinatario';

@Injectable()
export class DestinatarioService extends CrudService<Destinatario> {

    constructor(public override http: HttpClient) {
        super('/v1/email', http);
    }

    public get novoRegistro(): Observable<Destinatario> {
        return of({ email: '' });
    }

}
