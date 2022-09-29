import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MaterialModule } from 'src/app/modules/material.module';

// shared
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';
import { TokenInterceptor } from 'src/app/shared/interceptors/token-interceptor.interceptor';

// aplicação
import { DestinatarioComponent } from './destinatario.page';
import { CardDestinatarioEdicaoComponent } from './cards/card-destinatario-edicao/card-destinatario-edicao.component';
import { CardDestinatarioPesquisaComponent } from './cards/card-destinatario-pesquisa/card-destinatario-pesquisa.component';

const routes: Routes = [
    { path: '', component: DestinatarioComponent }
];

@NgModule({
    declarations: [
        DestinatarioComponent,

        // cards
        CardDestinatarioEdicaoComponent,
        CardDestinatarioPesquisaComponent,
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
    ],
    exports: [],
    providers: [
        TokenInterceptor,

        // interceptors
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
    ]
})
export class DestinatarioModule { }
