import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmailGroupPage } from './email-group.page';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/modules/material.module';
import { CardEmailGroupPesquisaComponent } from 'src/app/pages/email-group/cards/card-email-group-pesquisa/card-email-group-pesquisa.component';
import { CardEmailGroupEdicaoComponent } from 'src/app/pages/email-group/cards/card-email-group-edicao/card-email-group-edicao.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TokenInterceptor } from 'src/app/shared/interceptors/token-interceptor.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { UuidSelfRegistrationButtonsHandleComponent } from 'src/app/pages/email-group/components/uuid-self-registration-buttons-handle/uuid-self-registration-buttons-handle.component';

const routes: Routes = [
  { path: '', component: EmailGroupPage }
];

@NgModule({
  declarations: [
    EmailGroupPage,
    CardEmailGroupPesquisaComponent,
    CardEmailGroupEdicaoComponent,
    UuidSelfRegistrationButtonsHandleComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    MaterialModule,
    SysPipesModule,
  ],
  providers: [
    TokenInterceptor,

    // interceptors
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
  ],
})
export class EmailGroupModule { }
