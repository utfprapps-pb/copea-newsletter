import { HttpClient, HttpParams } from '@angular/common/http';
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
        return of({ email: '', groups: [] });
    }

    /**
     * @description Retorna os destinatários do grupo
     */
    public buscarPorGrupo(grupoId: number): Observable<Destinatario[]> {
        const params = new HttpParams().append('groupId', grupoId + '');
        return this.http.get<Destinatario[]>(this.baseUrl + this.url + '/find-by-group', { params: params });
    }


}
