import { Pipe, PipeTransform } from '@angular/core';
import { GrupoDestinatario } from '../../grupo-destinatarios/model/grupo-destinatario';

@Pipe({
  name: 'destinatariosGrupos'
})
export class DestinatariosGruposPipe implements PipeTransform {

  transform(value: GrupoDestinatario[], showAll: boolean): any {
    if (!value || !value.length)
      return '-';

    if (showAll)
      return this.getAllGroups(value);

    const firstItem = value[0];
    return firstItem.id + ' - ' + firstItem.name + (value.length > 1 ? ', ... ' : '');
  }

  private getAllGroups(groups: GrupoDestinatario[]): string {
    let all: string = "";
    groups.forEach((value) => {
      console.log(value);
      all += (all !== "" ? ', ' : '') + value.id + ' - ' + value.name;
    })
    return all;
  }

}
