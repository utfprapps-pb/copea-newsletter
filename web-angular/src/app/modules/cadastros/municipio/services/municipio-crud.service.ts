import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { Municipio } from '../modelos/municipio';
import { environment } from 'src/environments/environment';

@Injectable()
export class MunicipioCrudService {

  constructor(private http: HttpClient) { }

  carregar(id: number) {
    const url = `${environment.api}/municipio/${id}`;
    return this.http.get<Municipio>(url).pipe(
      tap(
        resultado => {
          return of(resultado);
        }
      )
    );
  }

  incluir(municipio: Municipio) {
    const url = `${environment.api}/municipio`;
    return this.http.post<Municipio>(url, municipio).pipe(
      tap(
        resultado => {
          return of(resultado);
        }
      )
    );
  }

  atualizar(municipio: Municipio) {
    const url = `${environment.api}/municipio`;
    return this.http.put<Municipio>(url, municipio).pipe(
      tap(
        resultado => {
          return of(resultado);
        }
      )
    );
  }

  deletar(id: number) {
    const url = `${environment.api}/municipio/${id}`;
    return this.http.delete<any>(url).pipe(
      tap(
        resultado => {
          return of(resultado);
        }
      )
    );
  }

}
