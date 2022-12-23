import { environment } from 'src/environments/environment';
import { RecuperacaoSenhaDTO } from './../../models/recuperacao-senha-dto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RecuperacaoSenhaService {

  private url = environment.api;

  constructor(private httpClient: HttpClient) {

   }

  public enviarCodigoPorEmail(username: string) {
    return this.httpClient.post(`${this.url}/user/send-code-recover-password/username/${username}`, null);
  }

  public trocarSenha(recuperacaoSenhaDTO: RecuperacaoSenhaDTO) {
    return this.httpClient.post(`${this.url}/user/recover-password`, recuperacaoSenhaDTO);
  }

}
