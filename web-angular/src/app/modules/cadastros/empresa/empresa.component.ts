import { Component } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { EmpresaService } from 'src/app/services/empresa.service';

@Component({
  selector: 'app-empresa',
  templateUrl: './empresa.component.html',
  styleUrls: ['./empresa.component.css'],
  providers: [EmpresaService]
})
export class EmpresaComponent {

  public form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public service: EmpresaService
  ) {
    this.criarForm();
  }

  public criarForm(): void {
    this.form = this.fb.group({
      razaoSocial: [null, Validators.required],
      fantasia: [null, Validators.required],
      cnpj: [null, Validators.required],
    });
  }

  public get razaoSocial() {
    return this.form.get('razaoSocial') as AbstractControl<any, any>;
  }

  public get fantasia() {
    return this.form.get('fantasia') as AbstractControl<any, any>;
  }

  public get cnpj() {
    return this.form.get('cnpj') as AbstractControl<any, any>;
  }

  public salvar() {
    if (this.form.valid) {

    }
  }
}
