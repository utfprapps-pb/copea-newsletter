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
import { MatTabsModule } from '@angular/material/tabs';
import { ConfiguracaoAplicacaoComponent } from 'src/app/pages/configuracao/components/aplicacao/configuracao-aplicacao.component';
import { ConfiguracaoEmailComponent } from 'src/app/pages/configuracao/components/email/configuracao-email.component';
import { ConfiguracaoEmailService } from 'src/app/pages/configuracao/components/email/services/configuracao-email.service';
import { ConfiguracaoAplicacaoService } from 'src/app/pages/configuracao/components/aplicacao/services/configuracao-aplicacao.service';

@NgModule({
    declarations: [
        ConfiguracaoComponent,
        ConfiguracaoAplicacaoComponent,
        ConfiguracaoEmailComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,

        // modules
        MaterialModule,
        MatProgressSpinnerModule,
        MatDialogModule,
        MatTabsModule,

        // shared
        SysPipesModule,
    ],
    exports: [],
    providers: [
        TokenInterceptor,
        // interceptors
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
        ConfiguracaoEmailService,
        ConfiguracaoAplicacaoService,
    ],
})
export class ConfiguracaoModule { }
