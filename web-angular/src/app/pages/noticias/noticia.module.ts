import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MaterialModule } from 'src/app/modules/material.module';

// text-editor
import { AngularEditorModule } from '@kolkov/angular-editor';

// shared
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';

// formulário
import { FormularioModule } from 'src/app/modules/formulario/formulario.module';

// aplicação
import { NoticiaComponent } from './noticia.page';
import { CardNoticiaTextoComponent } from './cards/card-noticia-texto/card-noticia-texto.component';
import { CardNoticiaCabecalhoComponent } from './cards/card-noticia-cabecalho/card-noticia-cabecalho.component';

const routes: Routes = [
    { path: '', component: NoticiaComponent }
];

@NgModule({
    declarations: [
        NoticiaComponent,
        
        // cards
        CardNoticiaCabecalhoComponent,
        CardNoticiaTextoComponent,
    ],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule,

        // material
        MaterialModule,

        // text-editor
        AngularEditorModule,

        // shared
        SysPipesModule,
        
        // formulário
        FormularioModule,

    ],
    exports: [],
    providers: [],
})
export class NoticiaModule { }
