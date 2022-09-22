import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', loadChildren: () => import('./pages/login/login.module').then(mod => mod.LoginModule) },
    { path: 'cadastro', loadChildren: () => import('./pages/cadastro/cadastro.module').then(mod => mod.CadastroModule) },
    { path: 'admin', loadChildren: () => import('./pages/admin/admin.module').then(mod => mod.AdminModule) },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
