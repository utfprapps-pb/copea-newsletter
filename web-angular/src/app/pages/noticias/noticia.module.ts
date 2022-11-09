import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

// material
import { MaterialModule } from 'src/app/modules/material.module';

// shared
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';

// aplicação
import { NoticiaComponent } from './noticia.page';
import { CardNoticiaTextoComponent } from './cards/card-noticia-texto/card-noticia-texto.component';
import { CardNoticiaCabecalhoComponent } from './cards/card-noticia-cabecalho/card-noticia-cabecalho.component';
import { TokenInterceptor } from 'src/app/shared/interceptors/token-interceptor.interceptor';

import { EditorModule } from '@tinymce/tinymce-angular';

const routes: Routes = [
    { path: '', component: NoticiaComponent },
    { path: ':id', component: NoticiaComponent },
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

        // shared
        SysPipesModule,

        EditorModule,
    ],
    exports: [],
    providers: [
        TokenInterceptor,
        // interceptors
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
    ],
})
export class NoticiaModule { }
