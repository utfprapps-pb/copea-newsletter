import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EmpresaComponent } from './empresa.component';


const routes: Routes = [
  {
    path: '', redirectTo: 'novo', pathMatch: 'full'
  },
  {
    path: 'novo', component: EmpresaComponent
  },
  {
    path: ':id', component: EmpresaComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmpresaRoutingModule { }
