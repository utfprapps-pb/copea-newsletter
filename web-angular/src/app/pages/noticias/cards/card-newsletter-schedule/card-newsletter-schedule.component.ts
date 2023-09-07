import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { NewsletterQuartzTasksService } from 'src/app/pages/noticias/services/newsletter-quartz-tasks.service';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-card-newsletter-schedule',
  templateUrl: './card-newsletter-schedule.component.html',
  styleUrls: ['./card-newsletter-schedule.component.scss'],
  providers: [
    NewsletterQuartzTasksService,
  ],
})
export class CardNewsletterScheduleComponent implements OnInit {

  public form: FormGroup;

  constructor(
    public formBuilder: FormBuilder,
    public dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public dataDialog: any,
    private newsletterQuartzTasksService: NewsletterQuartzTasksService,
    private mensagemService: MensagemService,
  ) {
    this.form = this.criarForm();
    this.implementChanges();
    this.configRecurrentFields(this.recurrent.value);
  }

  ngOnInit() {
  }

  private implementChanges() {
    this.recurrent.valueChanges.subscribe(this.recurrentChange.bind(this));
  }

  private recurrentChange(value) {
    this.configRecurrentFields(value);
  }

  private configRecurrentFields(enable) {
    if (enable) {
      this.dayRange.enable();
      this.dayRange.setValidators([Validators.required]);
      this.endAt.enable();
      this.endAt.setValidators([Validators.required]);
      return;
    }

    this.dayRange.disable();
    this.dayRange.clearValidators();
    this.endAt.disable();
    this.endAt.clearValidators();
  }

  public criarForm(): FormGroup {
    return this.formBuilder.group({
      id: [null],
      startAt: [new Date(), Validators.required],
      recurrent: [false],
      dayRange: [null],
      endAt: [null],
      time: ['01:23'],
    })
  }

  public get startAt(): AbstractControl<any, any> {
    return this.form.get('startAt') as AbstractControl<any, any>;
  }

  public get recurrent(): AbstractControl<any, any> {
    return this.form.get('recurrent') as AbstractControl<any, any>;
  }

  public get dayRange(): AbstractControl<any, any> {
    return this.form.get('dayRange') as AbstractControl<any, any>;
  }

  public get endAt(): AbstractControl<any, any> {
    return this.form.get('endAt') as AbstractControl<any, any>;
  }

  public dateChanged(date: Date) {
    this.startAt?.setValue(date);
  }

  public onAgendarEnvioClick() {
    console.log(this.form);
    if (this.form.invalid)
      return;

    console.log(this.dataDialog);

    if (this.dataDialog?.newsletter)
      this.newsletterQuartzTasksService.schedule({ newsletter: this.dataDialog.newsletter, quartzTask: this.form.value }).subscribe({
        next: (value) => {
          console.log(value);
          this.mensagemService.mostrarMensagem('Agendamento efetuado com sucesso.');
        },
        error: (error) => {
          console.log(error);
          let messageError = '';
          if (error?.error?.message)
            messageError = ` Detalhes: ${error.error.message}`;

          this.mensagemService.mostrarMensagem(`Erro ao agendar envio da newsletter por email.${messageError}`);
        }
      })
  }

  public onCancelarClick() {
    this.dialog.closeAll();
  }

}
