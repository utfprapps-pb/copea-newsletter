export interface CrudComponent<T> {

    loading: boolean;

    verificarCrudRouteParams(): void;

    persistirAlteracoes(isEdicao: boolean): void;

    salvar(registro: T): void;

    atualizar(registro: T): void;

    carregar(registroId: number): void;

    remover(registroId: number): void;

    validarForm(): boolean;
    
}