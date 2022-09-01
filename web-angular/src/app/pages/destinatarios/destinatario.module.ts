import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MaterialModule } from 'src/app/modules/material.module';

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
    ],
    exports: [],
    providers: [],
})
export class DestinatarioModule { }
