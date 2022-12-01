import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

// material
import { MatSnackBar } from '@angular/material/snack-bar';

// shared
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { NoticiaService } from '../noticias/noticia.service';
import { Noticia } from '../noticias/models/noticia';

@Component({
  selector: 'app-pesquisa-noticia',
  templateUrl: 'pesquisa-noticia.page.html',
  providers: [FormBuilder]
})
export class PesquisaNoticiaComponent implements OnInit, OnDestroy {

  /**
   * @description FormGroup do filtro de busca
   */
  public form = this.formBuilder.group({
    descricao: [''],
    filtros: [ ['ENVIADAS', 'NAO_ENVIADAS'] ]
  });

  /**
   * @description Armazena os resultados da busca pelos noticias
   */
  public listaResultado: Noticia[];

  /**
   * @description Flag que controla a configuração de "busca automática"
   */
  public autoBusca?: boolean;

  /**
   * @description Flag que controla o estado "em carregamento" do componente
   */
  public loading: boolean;

  /**
   * @description Armazena as incrições de eventos do componente
   */
  private subscription?: Subscription;

  @Input() public pesquisaNoticiasModelos: Boolean = false;

  constructor(
    private noticiaService: NoticiaService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
  ) {
    this.listaResultado = [];
    this.loading = false;
  }

  ngOnInit() {
    this.autoBusca = localStorage.getItem('autoBusca') ? localStorage.getItem('autoBusca') !== 'false' : true;

    if (this.autoBusca) {
      this.implementChanges();
    }

    this.configurarTelaQuandoSelecaoNoticiaModelo();
  }

  private configurarTelaQuandoSelecaoNoticiaModelo() {
    if (!this.pesquisaNoticiasModelos)
      return;

    this.form.patchValue({
      filtros: ['ENVIADAS', 'NAO_ENVIADAS', 'MODELO']
    });
    this.filtrarNoticias();
  }

  private implementChanges() {
    this.subscription = new Subscription();
    this.setBuscaAutomaticaChangeFieldForm('descricao');
    this.setBuscaAutomaticaChangeFieldForm('filtros');
  }

  private setBuscaAutomaticaChangeFieldForm(fieldName: string) {
    this.subscription!.add(this.form.get(fieldName)!.valueChanges.pipe(debounceTime(300)).subscribe(() => this.filtrarNoticias()));
  }

  private removeChanges() {
    if (this.subscription && !this.subscription.closed) {
      this.subscription.unsubscribe();
    }
  }

  /**
   * @description Busca as noticias de acordo com o filtro informado
   */
  public filtrarNoticias() {
    console.log(this.form.value);
    this.loading = true;
    this.noticiaService.search(this.form.value).subscribe(res => {
      this.loading = false;
      this.listaResultado = res;

      if (!res || res.length === 0) {
        this.snackBar.open('Não foi possível encontrar nenhum registro correspondente aos filtros informados!', 'Ok');
      }
    }, error => {
      this.loading = false;
      this.snackBar.open(errorTransform(error), 'Ok');
    });
  }

  /**
   * @description Executa no toggleChange do do slide-toggle da busca automática
   */
  public onAutoBuscaChange() {
    this.autoBusca = !this.autoBusca;

    localStorage.setItem('autoBusca', this.autoBusca + '');

    if (this.autoBusca) {
      this.implementChanges();
    } else {
      this.removeChanges();
    }
  }

  ngOnDestroy(): void {
    this.removeChanges();
  }

  public getStylePadding(): string {
    return ((this.pesquisaNoticiasModelos) ? "d-pad-top" : "d-pad-card-container");
  }

}
