import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-data-simple',
  templateUrl: './dialog-data-simple.component.html',
  styleUrls: ['./dialog-data-simple.component.scss'],
  standalone: true,
  imports: [MatDialogModule, MatButtonModule]
})
export class DialogDataSimpleComponent implements OnInit {

  data = inject(MAT_DIALOG_DATA);

  constructor() { }

  ngOnInit(): void {
  }

}
