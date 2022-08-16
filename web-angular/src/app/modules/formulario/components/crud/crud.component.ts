import { Component, OnInit, ChangeDetectionStrategy, Input, OnDestroy } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AdminGuard } from 'src/app/services/admin.guard';
import { Api } from 'src/app/services/api';

@Component({
    selector: 'app-crud',
    templateUrl: './crud.component.html',
    styleUrls: ['./crud.component.css'],
    changeDetection: ChangeDetectionStrategy.Default,
})
export class CrudComponent implements OnInit, OnDestroy {

    @Input() public form: NgForm;

    @Input() public titulo: string;

    @Input() public api: Api<any>;

    public registroId: number;

    private subscription: Subscription;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private adminGuard: AdminGuard
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.ler(params['id']);
        });
    }

    private ler(id: number) {
        if (id) {
            this.registroId = id;
            this.api.ler(id).subscribe(registro => {
                if (registro) {
                    this.form.form.patchValue(registro);
                }
            }, error => {
                console.error(error);
                alert('Não foi possível encontrar  o registro ' + id);
                this.abrirNovoRegistro();
            });
        }
    }

    public salvar() {
        if (this.form.valid) {
            if (this.registroId) {
                this.alterar();
            } else {
                this.criar();
            }
        }
    }

    private alterar() {
        const value = this.form.form.value;
        value.id = this.registroId;
        this.api.alterar(value, this.registroId).subscribe(registro => {
            if (registro) {
                this.form.form.patchValue(registro);
                alert(`Registro ${this.registroId} foi salvo com sucesso`);
            }
        }, error => {
            console.error(error);
            alert('Erro ao alterar o registro');
        });
    }

    private criar() {
        this.api.criar(this.form.form.value).subscribe((registro) => {
            // FIXME: Realizar ação após gravar o registro
            alert(`O registro com código ${registro.id} foi salvo com sucesso!`);
            this.limpar();
        }, error => {
            console.error(error);
            alert('Ocorreu um erro ao salvar o registro registro');
        });
    }

    public limpar() {
        if (this.registroId) {
            this.abrirNovoRegistro();
        } else {
            this.form.resetForm();
        }
    }

    private abrirNovoRegistro() {
        let url = this.adminGuard.url;
        url = url.replace(this.registroId.toString(), 'novo');
        this.router.navigateByUrl(url);
    }

    public deletar() {
        this.api.deletar(this.registroId).subscribe(() => {
            alert('Registro deletado com sucesso!');
            this.limpar();
        }, error => {
            console.error(error);
            alert('Ocorreu uma falha ao deletar o registro');
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
