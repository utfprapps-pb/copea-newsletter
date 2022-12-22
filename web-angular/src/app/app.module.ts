import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// modules
import { MaterialModule } from './modules/material.module';

// shared
import { TokenInterceptor } from './shared/interceptors/token-interceptor.interceptor';

// pages
import { CadastroModule } from './pages/cadastro/cadastro.module';
import { LoginModule } from './pages/login/login.module';

// aplicação
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DrawerService } from './pages/admin/drawer.service';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
    CadastroModule,
    LoginModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    TokenInterceptor,
    DrawerService,
    // interceptors
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true, },
  ]
})
export class AppModule { }
