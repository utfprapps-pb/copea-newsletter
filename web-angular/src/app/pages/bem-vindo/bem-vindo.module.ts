import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BemVindoComponent } from './bem-vindo.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', component: BemVindoComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
  ],
  declarations: [BemVindoComponent]
})
export class BemVindoModule { }
