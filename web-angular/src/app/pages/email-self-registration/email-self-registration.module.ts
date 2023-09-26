import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmailSelfRegistrationComponent } from './email-self-registration.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/modules/material.module';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { DestinatarioService } from 'src/app/pages/destinatarios/destinatario.service';

const routes: Routes = [
  { path: 'group/:uuid', component: EmailSelfRegistrationComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    SysPipesModule,
  ],
  declarations: [EmailSelfRegistrationComponent],
  providers: [
    DestinatarioService,
  ]
})
export class EmailSelfRegistrationModule { }
