import { MensagemService } from './../../shared/services/mensagem.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, AbstractControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

// shared
import { errorTransform } from 'src/app/shared/pipes/error-transform';

// aplicação
import { NoticiaService } from '../noticias/services/noticia.service';
import { Noticia } from '../noticias/models/noticia';
import { NewsletterSearchRequest } from '../noticias/models/newsletter-search-request';

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
    filtros: [['ENVIADAS', 'NAO_ENVIADAS']]
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

  @Input() public pesquisaNoticiasModelos: boolean = false;

  constructor(
    private noticiaService: NoticiaService,
    private formBuilder: FormBuilder,
    private mensagemService: MensagemService,
  ) {
    this.listaResultado = [];
    this.loading = false;
  }

  ngOnInit() {
    if (this.pesquisaNoticiasModelos)
      this.autoBusca = true;
    else
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
      filtros: ['MODELO', 'MODELOS_COMPARTILHADOS']
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
    this.loading = true;
    let filtrosDisponiveis: Array<string> = this.filtros?.value;
    let newsletterSearchRequest: NewsletterSearchRequest = {
      newslettersSent: filtrosDisponiveis.includes('ENVIADAS'),
      newslettersNotSent: filtrosDisponiveis.includes('NAO_ENVIADAS'),
      newslettersTemplateMine: filtrosDisponiveis.includes('MODELO'),
      newslettersTemplateShared: filtrosDisponiveis.includes('MODELOS_COMPARTILHADOS'),
      description: this.description?.value,
    }
    this.noticiaService.search(newsletterSearchRequest).subscribe({
      next: (res: Noticia[]) => {
        this.loading = false;
        this.listaResultado = res;

        if (!res || res.length === 0) {
          this.mensagemService.mostrarMensagem('Nenhum registro encontrado para os filtros informados.');
        }
      },
      error: (error) => {
        this.loading = false;
        this.mensagemService.mostrarMensagem(errorTransform(error));
      }
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


  private get description(): AbstractControl | null {
    return this.form.get('description');
  }

  private get filtros(): AbstractControl | null {
    return this.form.get('filtros');
  }

}
