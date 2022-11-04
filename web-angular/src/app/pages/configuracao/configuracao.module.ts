import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// material
import { MatDialogModule } from '@angular/material/dialog';

// shared
import { TokenInterceptor } from 'src/app/shared/interceptors/token-interceptor.interceptor';
import { SysPipesModule } from 'src/app/shared/pipes/sys-pipes.module';

// modules
import { MaterialModule } from 'src/app/modules/material.module';

// aplicação
import { ConfiguracaoComponent } from './configuracao.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
    declarations: [
        ConfiguracaoComponent
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
export class ConfiguracaoModule { }
