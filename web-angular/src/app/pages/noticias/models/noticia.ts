import { Destinatario } from "../../destinatarios/model/destinatario";

export interface Noticia {
    id?: number;
    descricao?: string; // Título
    emails?: Destinatario[];
    dataInclusao?: Date;
    dataAlteracao?: Date;
}