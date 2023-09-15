import { MatSnackBar } from '@angular/material/snack-bar';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MensagemService {

  constructor(public snackBar: MatSnackBar,) { }

  public mostrarMensagem(message: string) {
    this.snackBar.open(message, 'OK', {duration: 5000});
  }

}
