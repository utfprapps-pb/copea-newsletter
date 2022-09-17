import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'iddescription'
})
export class IddescriptionPipe implements PipeTransform {

    transform(value: any, padrao?: string, key = 'description'): any {
        return value ? value[key] : (padrao || '');
    }

}