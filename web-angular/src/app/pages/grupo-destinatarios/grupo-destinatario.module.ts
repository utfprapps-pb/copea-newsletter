import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// shared
import { TokenInterceptor } from 'src/app/shared/interceptors/token-interceptor.interceptor';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';

// modules
import { MaterialModule } from 'src/app/modules/material.module';

// aplicação
import { GrupoDestinatarioDialogComponent } from './components/grupo-destinatario-dialog/grupo-destinatario-dialog.component';
import { GrupoDestinatarioPesquisaDialogComponent } from './components/grupo-destinatario-pesquisa-dialog/grupo-destinatario-pesquisa-dialog.component';

@NgModule({
    declarations: [
        GrupoDestinatarioDialogComponent,
        GrupoDestinatarioPesquisaDialogComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,

        // modules
        MaterialModule,
        MatProgressSpinnerModule,
        MatDialogModule,

        // shared
        SysPipesModule,
    ],
    exports: [],
    providers: [
        TokenInterceptor,

        // interceptors
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
    ],
})
export class GrupoDestinatarioModule { }
