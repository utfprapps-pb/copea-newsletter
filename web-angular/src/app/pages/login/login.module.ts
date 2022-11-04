import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// modules
import { MaterialModule } from 'src/app/modules/material.module';

// aplicação
import { LoginComponent } from './login.component';

import { MatTabsModule } from '@angular/material/tabs';

const routes: Routes = [
    { path: '', component: LoginComponent }
];

@NgModule({
    declarations: [LoginComponent],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        MatTabsModule
    ],
    exports: [],
    providers: [],
})
export class LoginModule { }
