import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { MunicipioPesquisaComponent } from './pesquisa/municipio-pesquisa.component';
import { MunicipioFormComponent } from './formulario/municipio-form.component';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';

const routes: Routes = [
  { path: 'pesquisa',  component: MunicipioPesquisaComponent },
  { path: 'novo',  component: MunicipioFormComponent },
  { path: ':id',  component: MunicipioFormComponent }
];

@NgModule({
  declarations: [
    MunicipioPesquisaComponent,
    MunicipioFormComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,
    InputTextModule,
    DropdownModule,
    ButtonModule,
    PanelModule,
    TableModule,
    ToastModule
  ]
})
export class MunicipioModule { }
