import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-email-self-registration',
  templateUrl: './email-self-registration.component.html',
  styleUrls: ['./email-self-registration.component.css']
})
export class EmailSelfRegistrationComponent implements OnInit {

  public form: FormGroup;
  public pendingSubscribe: boolean = true;

  constructor(
    public route: ActivatedRoute,
    private formBuilder: FormBuilder,
  ) {
    this.buildForm();
  }

  ngOnInit() {
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      email: [null, [ Validators.required, Validators.email ]],
    });
  }

  public get email() {
    return this.form.get('email') as AbstractControl<any, any>;
  }

  public onSubscribe() {
    // TODO: Teste
    this.pendingSubscribe = false;
  }

  // getParam('uuid')
  public getParam(name: string): string | null {
		return this.route.snapshot.paramMap.get(name);
	}

}
