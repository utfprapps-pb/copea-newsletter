import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'idDescricao'
})
export class IdDescricaoPipe implements PipeTransform {

    transform(value: any, padrao?: string, key = 'descricao'): any {
        return value ? value[key] : (padrao || '');
    }

}