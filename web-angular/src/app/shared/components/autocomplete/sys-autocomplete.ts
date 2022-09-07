import { HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { errorTransform } from '../../pipes/error-transform';

export class SysAutocompleteControl {

    /**
     * @description Armazena os resultados filtrados
     */
    public optionsFiltradas?: Observable<any[]>;

    /**
     * @description Armazena todos os resultados buscados
     */
    private options?: any[];

    constructor(
        private buscaFn: Function,
        private snackBar: MatSnackBar,
        private filterKey?: string
    ) {
        if (!this.filterKey) {
            this.filterKey = 'descricao'
        }
    }

    /**
     * @description Busca as options na api
     */
    public onFocusAutocomplete() {
        this.buscarOptions();
    }

    /**
     * @description Busca as options do autoComplete
     */
    private buscarOptions() {
        this.buscaFn().subscribe((res: any[]) => {
            this.options = res;
            this.optionsFiltradas = of(this.options);
        }, (error: HttpErrorResponse) => {
            this.snackBar.open(errorTransform(error), 'Ok');
        });
    }

    /**
     * @description Filtra os interesses pelo valor inserido no auto-complete
     * @param input Valor do input
     */
    public filtrar(input: string) {
        if (!this.options) {
            return;
        }

        if (input && input.length > 0) {
            const filterValue = input.toLowerCase();
            this.optionsFiltradas = of(this.options.filter(item => item[this.filterKey!].toLowerCase().includes(filterValue)));
        } else {
            this.optionsFiltradas = of(this.options);
        }
    }

}