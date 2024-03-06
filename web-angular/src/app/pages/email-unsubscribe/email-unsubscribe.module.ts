import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmailUnsubscribeComponent } from './email-unsubscribe.component';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { MaterialModule } from 'src/app/modules/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: ':uuid', component: EmailUnsubscribeComponent }
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
  declarations: [EmailUnsubscribeComponent],
  providers: [
  ]
})
export class EmailUnsubscribeModule { }
