import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';
import { Usuario } from './../models/usuario';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  findUserByUsernameOrEmail(username: string, email: string): Observable<HttpResponse<Usuario>> {
    return this.http.get<HttpResponse<Usuario>>(`${environment.api}/user/exists`, {
      params: {
        username: username,
        email: email
      }
    });
  }

}
