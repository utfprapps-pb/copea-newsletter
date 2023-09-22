import { HttpErrorResponse } from "@angular/common/http";

export const ERROR_DETAIL = '\nPor favor, tente novamente mais tarde ou entre em contato com o suporte técnico.'

export function errorTransform(error: HttpErrorResponse): string {
  if (error) {
    if (error.error?.message) {
      return error.error.message;
    }

    if (error.status === 401) {
      return 'Sem autorização. Por favor, realize login no sistema.';
    }

    if (error.message) {
      switch (error.status) {
        case 403:
          return 'Acesso inválido' + ERROR_DETAIL;
        case 400:
          return error.message;
        default:
          return error.message + ERROR_DETAIL;
      }
    }
  }
  return 'Não foi possível concluir a operação' + ERROR_DETAIL;
}
