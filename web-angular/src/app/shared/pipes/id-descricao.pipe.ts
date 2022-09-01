import { Pipe, PipeTransform } from '@angular/core';

// aplicação
import { IdDescricao } from '../models/id-descricao';

@Pipe({
    name: 'idDescricao'
})
export class IdDescricaoPipe implements PipeTransform {

    transform(value: IdDescricao, padrao?: string): any {
        return value ? value.descricao : (padrao || '');
    }

}