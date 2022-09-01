import { HttpErrorResponse } from "@angular/common/http";

export const ERROR_DETAIL = '. Por favor, tente novamente mais tarde ou entre em contato com o suporte técnico.'

export function errorTransform(error: HttpErrorResponse): string {
    if (error) {
        if (error.error && error.error.message) {
            return error.error.message + ERROR_DETAIL;
        }
        if (error.message) {
            switch (error.status) {
                case 403:
                    return 'Acesso inválido' + ERROR_DETAIL;
                default:
                    return error.message + ERROR_DETAIL;
            }
        }
    }
    return 'Não foi possível concluir a operação' + ERROR_DETAIL;
}