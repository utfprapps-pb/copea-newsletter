import { HttpStatusCode } from '@angular/common/http';
import { Component, Inject, OnInit, Optional } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import * as moment from 'moment-timezone';
import { NewsletterQuartzTasksService } from 'src/app/pages/noticias/services/newsletter-quartz-tasks.service';
import { MensagemService } from 'src/app/shared/services/mensagem.service';

@Component({
  selector: 'app-card-newsletter-schedule',
  templateUrl: './card-newsletter-schedule.component.html',
  styleUrls: ['./card-newsletter-schedule.component.scss'],
})
export class CardNewsletterScheduleComponent implements OnInit {

  public form: FormGroup;

  constructor(
    public formBuilder: FormBuilder,
    public dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public dataDialog: any,
    @Optional() public dialogRef: MatDialogRef<CardNewsletterScheduleComponent>,
    private newsletterQuartzTasksService: NewsletterQuartzTasksService,
    private mensagemService: MensagemService,
  ) {
    this.form = this.criarForm();
    this.implementChanges();
    this.configRecurrentFields(this.recurrent.value);
  }

  ngOnInit() {
  }

  public criarForm(): FormGroup {
    return this.formBuilder.group({
      id: [null],
      startAt: [new Date(), Validators.required],
      timeStartAt: [this.getTimeString(new Date()), Validators.required],
      recurrent: [false],
      dayRange: [null],
      endAt: [null],
      timeEndAt: [null],
    })
  }

  private implementChanges() {
    this.recurrent.valueChanges.subscribe(this.recurrentChange.bind(this));
    this.startAt.valueChanges.subscribe(this.startAtChange.bind(this));
    this.timeStartAt.valueChanges.subscribe(this.timeStartAtChange.bind(this));
    this.endAt.valueChanges.subscribe(this.endAtChange.bind(this));
    this.timeEndAt.valueChanges.subscribe(this.timeEndAtChange.bind(this));
  }

  private recurrentChange(value) {
    this.configRecurrentFields(value);
  }

  private startAtChange(startDate) {
    if (startDate && this.timeStartAt?.value)
      this.joinTimeOnDate(startDate, this.timeStartAt.value);
  }

  private timeStartAtChange(startTime) {
    if (this.startAt?.value && startTime)
      this.joinTimeOnDate(this.startAt.value, startTime);
  }

  private endAtChange(endDate) {
    if (endDate && this.timeEndAt?.value)
      this.joinTimeOnDate(endDate, this.timeEndAt.value);
  }

  private timeEndAtChange(endTime) {
    if (this.endAt?.value && endTime)
      this.joinTimeOnDate(this.endAt.value, endTime);
  }

  private joinTimeOnDate(date: Date, time: string) {
    if (date && time) {
      // Quando o valor da string vindo for exemplo 01:10 AM ou 01:10 PM
      // let parts = time.match(/(\d+)\:(\d+) (\w+)/);

      // Quando o valor da string vindo for exemplo 09:10 ou 19:10
      let parts = time.match(/(\d+)\:(\d+)/);
      if (parts) {
        // Quando o valor da string vindo for exemplo 01:10 AM ou 01:10 PM
        // const hours = /am/i.test(parts[3]) ? parseInt(parts[1], 10) : parseInt(parts[1], 10) + 12;

        // Quando o valor da string vindo for exemplo 09:10 ou 19:10
        const hours = parseInt(parts[1], 10);

        const minutes = parseInt(parts[2], 10);
        date.setHours(hours);
        date.setMinutes(minutes);
      }
    }
  }

  private configRecurrentFields(enable) {
    if (enable) {
      this.dayRange.enable();
      this.setValidators(this.dayRange, [Validators.required]);

      this.endAt.enable();
      this.setValidators(this.endAt, [Validators.required]);

      this.timeEndAt.enable();
      this.setValidators(this.timeEndAt, [Validators.required]);
      return;
    }

    this.dayRange.disable();
    this.clearValidators(this.dayRange);

    this.endAt.disable();
    this.clearValidators(this.endAt);

    this.timeEndAt.disable();
    this.clearValidators(this.timeEndAt);
  }

  private setValidators(formField: AbstractControl, validators: ValidatorFn[]) {
    formField.setValidators(validators);
    formField.updateValueAndValidity();
  }

  private clearValidators(formField: AbstractControl) {
    formField.clearValidators();
    formField.updateValueAndValidity();
  }

  private getTimeString(date: Date) {
    // Formata a hora e minutos em 00:00
    const hours = date.getHours()?.toString().padStart(2, '0');
    const minutes = date.getMinutes()?.toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  public get startAt(): AbstractControl<any, any> {
    return this.form.get('startAt') as AbstractControl<any, any>;
  }

  public get timeStartAt(): AbstractControl<any, any> {
    return this.form.get('timeStartAt') as AbstractControl<any, any>;
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

  public get timeEndAt(): AbstractControl<any, any> {
    return this.form.get('timeEndAt') as AbstractControl<any, any>;
  }

  public dateChanged(date: Date) {
    this.startAt?.setValue(date);
  }

  public onAgendarEnvioClick() {
    if (this.form.invalid)
      return;

    if (this.dataDialog?.newsletter) {
      let formValue = this.form.value;
      this.newsletterQuartzTasksService.schedule({
        newsletter: this.dataDialog.newsletter,
        quartzTask: {
          startAt: this.getFormatDateFromMoment(formValue?.startAt),
          recurrent: formValue?.recurrent,
          dayRange: formValue?.dayRange,
          endAt: this.getFormatDateFromMoment(formValue?.endAt),
        }
      }).subscribe({
        next: (value) => {
          console.log(value);
          this.mensagemService.mostrarMensagem('Agendamento efetuado com sucesso.');
          // TODO: ajustar para retornar um objeto para controlar se fez o agendamento
          // antes de fechar a dialog.
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.log(error);
          if ((error.status == HttpStatusCode.BadRequest) && error?.error?.message)
            this.mensagemService.mostrarMensagem(error.error.message);
          else
            this.showMessageWhenError(error);
        }
      })
    }
  }

  private showMessageWhenError(error) {
    let messageError = '';
    if (error?.error?.message)
      messageError = ` Detalhes: ${error.error.message}`;
    this.mensagemService.mostrarMensagem(`Erro ao agendar envio da newsletter por email.${messageError}`);
  }

  public onCancelarClick() {
    this.closeDialog();
  }

  private closeDialog() {
    this.dialog.closeAll();
  }

  private getFormatDateFromMoment(date: Date): Date {
    if (!date)
      return date;

    let formatDate = moment(date)?.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
    return new Date(formatDate);
  }

}
