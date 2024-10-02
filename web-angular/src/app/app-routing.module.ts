import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// shared
import { RoleGuard } from './shared/guards/role.guard';

const routes: Routes = [
    { path: '', redirectTo: 'admin/bem-vindo', pathMatch: 'full' },
    { path: 'login', loadChildren: () => import('./pages/login/login.module').then(mod => mod.LoginModule) },
    { path: 'recuperacao-senha', loadChildren: () => import('./pages/recuperacao-senha/recuperacao-senha.module').then(mod => mod.RecuperacaoSenhaModule) },
    { path: 'cadastro', loadChildren: () => import('./pages/cadastro/cadastro.module').then(mod => mod.CadastroModule) },
    { path: 'email-self-registration', loadChildren: () => import('./pages/email-self-registration/email-self-registration.module').then(mod => mod.EmailSelfRegistrationModule) },
    { path: 'email-unsubscribe', loadChildren: () => import('./pages/email-unsubscribe/email-unsubscribe.module').then(mod => mod.EmailUnsubscribeModule) },
    { path: 'admin', loadChildren: () => import('./pages/admin/admin.module').then(mod => mod.AdminModule), canActivate: [RoleGuard] },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule],
    providers: [
      RoleGuard,
    ]
})
export class AppRoutingModule { }
