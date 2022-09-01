import { FormBuilder, FormGroup } from "@angular/forms";

// aplicação
import { CrudCard } from "./crud-card";
import { AdvancedCrudController } from "./advanced-crud.controller";

export abstract class AdvancedCrudCard<T> implements CrudCard<T> {

    form: FormGroup;

    constructor(
        public crudController: AdvancedCrudController<T>,
        public formBuilder: FormBuilder,
    ) {
        this.crudController.registerCard(this);
        this.form = this.criarForm();
    }

    abstract criarForm(): FormGroup;

    setForm(registro: T): void {
        this.form.reset(registro);
    }

    setRegistro(registro: T): void {
        Object.assign(registro, this.form.getRawValue())
    }

    validarForm(): boolean {
        this.form.markAllAsTouched();
        this.form.updateValueAndValidity();
        return this.form.valid;
    }

}