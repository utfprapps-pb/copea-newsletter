import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable()
export class CrudController {

    private _operacaoConcluidaEvent: Subject<void> = new Subject();
    private _registroCarregadoEvent: Subject<void> = new Subject();

    public notificarOperacaoConcluida() {
        this._operacaoConcluidaEvent.next();
    }

    public get onOperacaoConcluida(): Observable<void> {
        return this._operacaoConcluidaEvent.asObservable();
    }

    public notificarCarga() {
        this._registroCarregadoEvent.next();
    }

    public get onCarregarRegistro(): Observable<void> {
        return this._registroCarregadoEvent.asObservable();
    }
    
}