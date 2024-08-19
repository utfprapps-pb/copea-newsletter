import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfiguracaoAplicacaoComponent } from 'src/app/pages/configuracao/components/aplicacao/configuracao-aplicacao.component';
import { ConfiguracaoEmailComponent } from 'src/app/pages/configuracao/components/email/configuracao-email.component';

@Component({
    selector: 'app-configuracao',
    templateUrl: './configuracao.component.html',
    styleUrls: ['./configuracao.component.css'],
    providers: []
})
export class ConfiguracaoComponent {

  @ViewChild(ConfiguracaoEmailComponent) public configuracaoEmailComponent: ConfiguracaoEmailComponent;
  @ViewChild(ConfiguracaoAplicacaoComponent) public configuracaoAplicacaoComponent: ConfiguracaoAplicacaoComponent;

  constructor(
    public dialog: MatDialog,
  ) {

  }

  public gravar() {
    this.configuracaoEmailComponent.persistirAlteracoes();
    this.configuracaoAplicacaoComponent.persistirAlteracoes();

    this.dialog.closeAll();
  }

}
