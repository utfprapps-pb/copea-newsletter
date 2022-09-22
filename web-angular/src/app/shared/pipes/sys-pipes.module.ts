import { NgModule } from '@angular/core';
import { ErroPipe } from './erro.pipe';

// aplicação
import { IddescriptionPipe } from './id-description.pipe';

@NgModule({
    declarations: [
        IddescriptionPipe,
        ErroPipe,
    ],
    imports: [],
    exports: [
        IddescriptionPipe,
        ErroPipe,
    ],
    providers: [],
})
export class SysPipesModule { }
