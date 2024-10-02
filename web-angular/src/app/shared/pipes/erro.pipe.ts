import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'erro'
})
export class ErroPipe implements PipeTransform {

    transform(value: any): any {
      if (!value) {
        return null;
      }
      if (value['required']) {
        return 'Campo obrigatório';
      }
      if (value['email']) {
        return 'E-mail inválido';
      }
      if (value['maxlength']) {
        return `O campo deve possuir no máximo ${value['maxlength'].requiredLength} caracteres`;
      }
      if (value['minlength']) {
        return `O campo deve possuir no mínimo ${value['minlength'].requiredLength} caracteres`;
      }
      const chaves = Object.keys(value);
      return (chaves.length > 0) ? value[chaves[0]] : null;
    }

}
