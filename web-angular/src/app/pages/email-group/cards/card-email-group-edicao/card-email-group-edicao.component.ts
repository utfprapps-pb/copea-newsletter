import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-card-email-group-edicao',
  templateUrl: './card-email-group-edicao.component.html',
  styleUrls: ['./card-email-group-edicao.component.css'],
})
export class CardEmailGroupEdicaoComponent implements OnInit {

  @Input() public form!: FormGroup;

  private _resetFormNovo: Subject<void> = new Subject();

  /**
 * @description Evento de persistir o registro em edição
 */
  private _persistirEdicaoEvent: Subject<void> = new Subject();

  constructor() { }

  ngOnInit() {
  }

  public get resetFormNovoEvent(): Observable<void> {
    return this._resetFormNovo.asObservable();
  }

  public resetFormNovo() {
    this._resetFormNovo.next();
  }

  /**
   * @description Lança o evento de persistência da edição
   */
  public persistirEdicao(): void {
    this._persistirEdicaoEvent.next();
  }

  public get persistirEdicaoEvent(): Observable<void> {
    return this._persistirEdicaoEvent.asObservable();
  }

  public get name(): AbstractControl<any, any> {
    return this.form.get('name') as AbstractControl<any, any>;
  }

}
