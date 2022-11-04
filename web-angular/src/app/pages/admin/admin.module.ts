import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

// modules
import { MaterialModule } from 'src/app/modules/material.module';

// aplicação
import { AdminComponent } from './admin.component';
import { ConfiguracaoModule } from '../configuracao/configuracao.module';

const routes: Routes = [
    {
        path: '', 
        component: AdminComponent,
        children: [
            { path: 'destinatario', loadChildren: () => import('../destinatarios/destinatario.module').then(mod => mod.DestinatarioModule) },
            { path: 'noticia', loadChildren: () => import('../noticias/noticia.module').then(mod => mod.NoticiaModule) },
            { path: 'noticia-pesquisa', loadChildren: () => import('../pesquisa-noticia/pesquisa-noticia.module').then(mod => mod.PesquisaNoticiaModule) },
        ]
    },
];

@NgModule({
    declarations: [AdminComponent],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),

        // modules
        MaterialModule,

        // aplicação
        ConfiguracaoModule,
    ],
    exports: [AdminComponent],
    providers: [],
})
export class AdminModule { }
