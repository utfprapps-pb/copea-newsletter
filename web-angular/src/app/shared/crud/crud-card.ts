import { FormGroup } from "@angular/forms";

export interface CrudCard<T> {

    form: FormGroup;
    
    criarForm(): FormGroup;

    setForm(registro: T): void;

    setRegistro(registro: T): void;

}