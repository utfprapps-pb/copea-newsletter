import { LastSentEmailNewsletter } from '../../models/last-sent-email-newsletter';
import { DrawerService } from '../../../admin/drawer.service';
import { Component, ViewChild, OnInit, ElementRef, Output, Input, OnChanges, SimpleChanges, EventEmitter } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';

import { DomSanitizer } from '@angular/platform-browser';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';
import { QuartzTasks } from 'src/app/pages/noticias/cards/card-newsletter-schedule/models/quartz-tasks';
import { DatePipe } from '@angular/common';
import { QuartzTasksService } from 'src/app/pages/noticias/services/quartz-tasks.service';
import { MensagemService } from 'src/app/shared/services/mensagem.service';
import { errorTransform } from 'src/app/shared/pipes/error-transform';
import { FrontendConfigService } from 'src/app/services/frontend-config.service';

@Component({
  selector: 'app-card-noticia-texto',
  templateUrl: 'card-noticia-texto.component.html',
  styleUrls: ['./card-noticia-texto.component.scss']
})
export class CardNoticiaTextoComponent extends AdvancedCrudCard<Noticia> implements OnInit, OnChanges {

  @ViewChild('editor', { static: true }) public editorComponent: ElementRef;

  @Input() public lastSentEmailNewsletter: LastSentEmailNewsletter;

  @Input() public activeNewsletterQuartzTasksSchedules: Array<QuartzTasks>;
  public activeNewsletterQuartzTasksSchedulesText: string = '';

  @Output('cancelSchedule') public cancelScheduleEvent = new EventEmitter();

  public ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['activeNewsletterQuartzTasksSchedules'])
      this.setActiveNewsletterQuartzTasksSchedulesText();
  }

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override formBuilder: FormBuilder,
    public sanitizer: DomSanitizer,
    public drawerService: DrawerService,
    private datePipe: DatePipe,
    private quartzTasksService: QuartzTasksService,
    private mensagemService: MensagemService,
    private frontendConfigService: FrontendConfigService,
  ) {
    super(crudController, formBuilder);
  }

  override setRegistro(registro: any): void {
    // TESTE - TODO: colocar essa chamada em um listener pra sempre atualizar ou criar uma configuração pra setar manualmente,
    // pois quando executar a task agendada pode ser que não tenha atualizado no backend a url ainda e envie sem o replace da url.
    // OU toda vez que inicia o angular salvar em uma tabela a url, assim a task agendada pega a última url disponível
    this.frontendConfigService.update({ url: `${location.origin}/#/` }).subscribe();
    Object.assign(registro, this.form.getRawValue())
  }

  public get newsletter(): AbstractControl {
    return this.form.get('newsletter') as AbstractControl;
  }

  public get texto(): string {
    return this.newsletter.value;
  }

  public get id(): number {
    return this.form.get('id')?.value;
  }

  criarForm(): FormGroup {
    return this.formBuilder.group({
      id: [null],
      newsletter: [null, Validators.required],
      newsletterTemplate: [false],
    })
  }

  public getInitData() {
    return {
      language: 'pt_BR',
      selector: 'textarea',
      plugins: 'code anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount autoresize fullscreen preview',
      toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table | addcomment showcomments | spellcheckdialog | align lineheight | numlist bullist indent outdent | emoticons charmap | removeformat',
      paste_data_images: true,
      images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
        resolve('data:' + blobInfo.blob().type + ';base64,' + blobInfo.base64());
      },
      ),
      image_advtab: true,
      // init_instance_callback: function (editor) {
      //   editor.on('ExecCommand', function (e) {
      //     console.log('The ' + e.command + ' command was fired.');
      //   });
      // },
      setup: (editor) => { this.addEventFullscreenStateChanged(editor) },
      convert_urls: false,
    }
  }

  public addEventFullscreenStateChanged(editor: any) {
    editor.on('FullscreenStateChanged', (state: any) => {
      this.mostrarMenuConformeEditorEmFullscreen(editor, state.state);
    })
  }

  public mostrarMenuConformeEditorEmFullscreen(editor: any, inFullscreen: boolean) {
    if (inFullscreen)
      this.drawerService.drawer.close();
    else {
      this.drawerService.drawer.open();
      //Usado pois tem um bug quando o toggle é para abrir o menu, onde não chama o evento de open,
      //ao contrário do close, que chama, por isso esse código força a chamada de outro evento, que aí sim, chama o evento do open do menu
      editor.fire('blur');
      editor.focus();
    }
    //Usado pois ao fechar o menu, não atualiza sozinho
    this.drawerService.matDrawerContainer.updateContentMargins();
  }

  private setActiveNewsletterQuartzTasksSchedulesText() {
    this.activeNewsletterQuartzTasksSchedulesText = '';
    if ((!this.activeNewsletterQuartzTasksSchedules) ||
      (this.activeNewsletterQuartzTasksSchedules.length == 0))
      return;

    let activeNewsletterQuartzTaskSchedule = this.activeNewsletterQuartzTasksSchedules[0];
    let formatStartAtDate = this.datePipe.transform(activeNewsletterQuartzTaskSchedule.startAt, 'dd/MM/yyyy HH:mm:ss');
    let formatEndAtDate = this.datePipe.transform(activeNewsletterQuartzTaskSchedule.endAt, 'dd/MM/yyyy HH:mm:ss');

    if (activeNewsletterQuartzTaskSchedule.recurrent) {
      this.activeNewsletterQuartzTasksSchedulesText =
        `Existe um envio agendado para iniciar em
        ${formatStartAtDate} com envio recorrente de
        ${activeNewsletterQuartzTaskSchedule.dayRange} dias
        e data de término em ${formatEndAtDate}`;
      return;
    }
    this.activeNewsletterQuartzTasksSchedulesText = `Existe um envio agendado para ${formatStartAtDate}`;
  }

  public onCancelSchedule() {
    if (this.activeNewsletterQuartzTasksSchedules?.length == 0)
      return;

    let activeNewsletterQuartzTaskSchedule = this.activeNewsletterQuartzTasksSchedules[0];
    if (activeNewsletterQuartzTaskSchedule.id)
      this.quartzTasksService.cancel(activeNewsletterQuartzTaskSchedule.id).subscribe({
        next: (response) => {
          if (response.value) {
            this.mensagemService.mostrarMensagem('Agendamento cancelado com sucesso.');
            this.cancelScheduleEvent.emit();
          }
        },
        error: (error) => {
          this.mensagemService.mostrarMensagem(errorTransform(error) + '');
        }
      });
  }

}

