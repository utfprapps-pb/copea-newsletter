import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmailGroupPage } from './email-group.page';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/modules/material.module';
import { CardEmailGroupPesquisaComponent } from 'src/app/pages/email-group/cards/card-email-group-pesquisa/card-email-group-pesquisa.component';
import { CardEmailGroupEdicaoComponent } from 'src/app/pages/email-group/cards/card-email-group-edicao/card-email-group-edicao.component';

const routes: Routes = [
  { path: '', component: EmailGroupPage }
];

@NgModule({
  declarations: [
    EmailGroupPage,
    CardEmailGroupPesquisaComponent,
    CardEmailGroupEdicaoComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MaterialModule,
  ],
})
export class EmailGroupModule { }
