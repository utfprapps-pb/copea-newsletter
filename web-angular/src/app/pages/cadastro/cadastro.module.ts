import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// modules
import { MaterialModule } from 'src/app/modules/material.module';

// shared
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';

// aplicação
import { CadastroComponent } from './cadastro.component';

const routes: Routes = [
    { path: '', component: CadastroComponent }
];

@NgModule({
    declarations: [CadastroComponent],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        SysPipesModule,
    ],
    exports: [CadastroComponent],
    providers: [],
})
export class CadastroModule { }
