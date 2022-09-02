
import { Component, Input } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';

@Component({
    selector: 'app-card-destinatario-edicao',
    templateUrl: 'card-destinatario-edicao.component.html'
})
export class CardDestinatarioEdicaoComponent {

    /**
     * @description Recebe o FormGroup do card
     */
    @Input() public form!: FormGroup;
    
    /**
     * @description Evento de persistir o registro em edição
     */
    private _persistirEdicaoEvent: Subject<void> = new Subject();

    /**
     * @description Evento de remoção o registro em edição
     */
    private _removerRegistroEvent: Subject<number> = new Subject();

    constructor() { }

    public get email(): AbstractControl {
        return this.form.get('email')!;
    }

    /**
     * @description Lança o evento de persistência da edição
     */
    public persistirEdicao(): void {
        this._persistirEdicaoEvent.next();
    }
    
    /**
     * @description Lança o evento de remoção do registro
     */
    public removerRegistro(): void {
        if (this.form.get('id')?.value) {
            this._removerRegistroEvent.next(this.form.get('id')?.value);
        }
    }

    public get persistirEdicaoEvent(): Observable<void> {
        return this._persistirEdicaoEvent.asObservable();
    }

    public get removerRegistroEvent(): Observable<number> {
        return this._removerRegistroEvent.asObservable();
    }

}