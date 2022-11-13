import { Pipe, PipeTransform } from '@angular/core';
import { GrupoDestinatario } from '../../grupo-destinatarios/model/grupo-destinatario';

@Pipe({
    name: 'destinatariosGrupos'
})
export class DestinatariosGruposPipe implements PipeTransform {

    transform(value: GrupoDestinatario[]): any {
        if (value && value.length) {
            const firstItem = value[0];
            return firstItem.id + ' - ' + firstItem.name + (value.length > 1 ? ', ... ' : '');
        }
        return '-'
    }

}