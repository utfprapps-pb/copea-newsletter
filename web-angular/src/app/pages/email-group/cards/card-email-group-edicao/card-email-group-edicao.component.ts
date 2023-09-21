import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-card-email-group-edicao',
  templateUrl: './card-email-group-edicao.component.html',
  styleUrls: ['./card-email-group-edicao.component.css']
})
export class CardEmailGroupEdicaoComponent implements OnInit {

  @Input() form: FormGroup;

  constructor() { }

  ngOnInit() {
  }

}
