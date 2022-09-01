import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AdminComponent } from './pages/admin/admin.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';


const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'cadastro', component: CadastroComponent },
    {
        path: 'admin', component: AdminComponent,
        // canActivate: [AdminGuard],
        children: [
            { path: 'empresa', loadChildren: () => import('./modules/cadastros/empresa/empresa.module').then(x => x.EmpresaModule) },
            { path: 'teste', loadChildren: () => import('./modules/cadastros/municipio/municipio.module').then(mod => mod.MunicipioModule) },
            { path: 'destinatario', loadChildren: () => import('./pages/destinatarios/destinatario.module').then(mod => mod.DestinatarioModule) }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
