import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-input-password',
  templateUrl: './input-password.component.html',
  styleUrls: ['./input-password.component.css']
})
export class InputPasswordComponent {

  @Input() public control: AbstractControl;
  @Input() public label = 'Senha';
  @Input() public placeHolder = 'Informe sua senha';
  public hidePassword = true;

  constructor() { }

}
