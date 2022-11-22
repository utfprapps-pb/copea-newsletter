import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

// aplicação
import { PesquisaNoticiaResultadoComponent } from './components/pesquisa-noticia-resultado/pesquisa-noticia-resultado.component';
import { PesquisaNoticiaComponent } from './pesquisa-noticia.page';
import { NoticiaService } from '../noticias/noticia.service';

const routes: Routes = [
    { path: 'pesquisa', component: PesquisaNoticiaComponent }
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        FormsModule,
        ReactiveFormsModule,

        // material
        MatProgressSpinnerModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatCheckboxModule,
        MatButtonModule,
        MatInputModule,
        MatCardModule,
        MatIconModule,
    ],
    declarations: [
        PesquisaNoticiaComponent,

        // componentes
        PesquisaNoticiaResultadoComponent,
    ],
    exports: [
        PesquisaNoticiaComponent,
    ],
    providers: [
        NoticiaService,
    ],
})
export class PesquisaNoticiaModule { }
