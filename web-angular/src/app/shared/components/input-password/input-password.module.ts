import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { InputPasswordComponent } from 'src/app/shared/components/input-password/input-password.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    InputPasswordComponent,
  ],
  imports: [
    CommonModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    SysPipesModule,
    MatIconModule,
  ],
  exports: [
    InputPasswordComponent,
  ],
})
export class InputPasswordModule { }
