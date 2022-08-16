import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
// import { MessageService } from 'primeng/components/common/messageservice';
import { MunicipioCrudService } from '../services/municipio-crud.service';
import { MunicipioPesquisaService } from '../services/municipio-pesquisa.service';
import { Municipio } from '../modelos/municipio';

@Component({
  selector: 'app-municipio-form',
  templateUrl: './municipio-form.component.html',
  styleUrls: ['./municipio-form.component.css'],
  providers: [
    MunicipioCrudService,
    MunicipioPesquisaService
  ]
})
export class MunicipioFormComponent implements OnInit {

  ufs = [];
  formMunicipio: FormGroup;
  editando = false;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private route: ActivatedRoute,
              // private messageService: MessageService,
              private municipioPesquisaService: MunicipioPesquisaService,
              private municipioCrudService: MunicipioCrudService) {
    this.configurarFormulario();
  }

  ngOnInit() {
    this.carregarUfs();
    this.verificarParametroRota();
  }

  configurarFormulario() {
    this.formMunicipio = this.formBuilder.group({
      id: '',
      nome: ['', Validators.required],
      uf: ['', Validators.required]
    });
    this.formMunicipio.get('id')?.disable();
  }

  carregarUfs() {
    // this.municipioPesquisaService.listarUFs().subscribe(
    //   ufs => {
    //     this.ufs = ufs;
    //   }
    // );
  }

  verificarParametroRota(): void {
    // const id = +this.route.snapshot.paramMap.get('id');
    const id = 0; 
    if (id) {
      this.carregarMunicipio(id);
    } else {
      this.novo();
    }
  }

  carregarMunicipio(id: number) {
    this.municipioCrudService.carregar(id).subscribe(
      municipio => {
        this.formMunicipio.get('id')?.patchValue(municipio.id);
        this.formMunicipio.get('nome')?.patchValue(municipio.nome);
        this.formMunicipio.get('uf')?.patchValue({
          key: municipio.uf
        });
        this.editando = true;
      }
    );
  }

  getMunicipioDoForm(): Municipio {
    return {
      id: this.formMunicipio.get('id')?.value,
      nome: this.formMunicipio.get('nome')?.value,
      uf: this.formMunicipio.get('uf')?.value.key
    };
  }

  salvar() {
    if (this.validarFormulario()) {
      const municipio: Municipio = this.getMunicipioDoForm();
      if (municipio.id) {
        this.atualizarMunicipio(municipio);
      } else {
        this.incluirMunicipio(municipio);
      }
    } else {
      console.log("Não é possível salvar o Município!");
      // this.messageService.add({
      //   severity: 'warn',
      //   summary: 'Não é possível salvar o Município!',
      //   detail: 'Verifique o preenchimento dos campos e tente novamente.'
      // });
    }
  }

  atualizarMunicipio(municipio: Municipio) {
    this.municipioCrudService.atualizar(municipio).subscribe(
      municipioId => {
        // this.messageService.add({
        //   severity: 'success',
        //   summary: 'Sucesso!',
        //   detail: `Município ${municipio.nome} alterado com sucesso!`
        // });
      }
    );
  }

  incluirMunicipio(municipio: Municipio) {
    this.municipioCrudService.incluir(municipio).subscribe(
      municipioId => {
        // this.messageService.add({
        //   severity: 'success',
        //   summary: 'Sucesso!',
        //   detail: `Município ${municipio.nome} incluído com sucesso!`
        // });
        this.router.navigate([`/municipio/${municipioId}`]);
      }
    );
  }

  validarFormulario() {
    this.formMunicipio.markAsDirty();
    this.formMunicipio.updateValueAndValidity();
    return this.formMunicipio.valid;
  }

  excluir() {
    const id = this.formMunicipio.get('id')?.value;
    const confirmacao = confirm('Deseja excluir o Município ' + id + '?');
    if (confirmacao) {
      this.municipioCrudService.deletar(id).subscribe(
        resultado => {
          // this.messageService.add({
          //   severity: 'success',
          //   summary: 'Sucesso!',
          //   detail: `Município ${id} excluído com sucesso!`
          // });
          this.novo();
        }
      );
    }
  }

  novo() {
    this.editando = false;
    this.formMunicipio.reset({
      uf: {
        key: 'PR'
      }
    });
    this.router.navigate(['/municipio/novo']);
  }

  cancelar() {
    const id = this.formMunicipio.get('id')?.value;
    if (id) {
      this.carregarMunicipio(id);
    } else {
      this.novo();
    }
  }

}
