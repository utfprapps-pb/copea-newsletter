import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { MatTabsModule } from '@angular/material/tabs';
import { MaterialModule } from 'src/app/modules/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecuperacaoSenhaComponent } from './recuperacao-senha.component';

const routes: Routes = [
  { path: '', component: RecuperacaoSenhaComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    MatTabsModule,
    SysPipesModule
  ],
  declarations: [RecuperacaoSenhaComponent]
})
export class RecuperacaoSenhaModule { }
