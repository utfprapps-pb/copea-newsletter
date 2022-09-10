import { HttpClient } from '@angular/common/http';
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
        return of({ descricao: '' });
    }

}