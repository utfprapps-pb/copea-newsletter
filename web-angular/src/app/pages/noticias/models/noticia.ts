import { Destinatario } from "../../destinatarios/model/destinatario";

export interface Noticia {
    id?: number;
    description?: string; // Título
    subject?: string;
    newsletter?: string;
    newsletterTemplate?: boolean;
    emails?: Destinatario[];
}
