import { Pipe, PipeTransform } from '@angular/core';
import { EmailGroupRelation, GrupoDestinatario } from '../../grupo-destinatarios/model/grupo-destinatario';

@Pipe({
  name: 'destinatariosGrupos'
})
export class DestinatariosGruposPipe implements PipeTransform {

  transform(value: EmailGroupRelation[], showAll: boolean): any {
    if (!value || !value.length)
      return '-';

    if (showAll)
      return this.getAllGroups(value);

    const firstItem = value[0];
    return firstItem.emailGroup!.id + ' - ' + firstItem.emailGroup?.name + (value.length > 1 ? ', ... ' : '');
  }

  private getAllGroups(emailGroupRelations: EmailGroupRelation[]): string {
    let all: string = "";
    emailGroupRelations.forEach((emailGroupRelation) => {
      all += (all !== "" ? ', ' : '') + emailGroupRelation.emailGroup?.id + ' - ' + emailGroupRelation.emailGroup?.name;
    })
    return all;
  }

}
