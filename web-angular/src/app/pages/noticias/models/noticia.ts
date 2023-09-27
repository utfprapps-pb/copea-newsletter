import { NewsletterEmailGroup } from "src/app/pages/noticias/models/newsletter-email-group";
import { Destinatario } from "../../destinatarios/model/destinatario";

export interface Noticia {
    id?: number;
    description?: string; // Título
    subject?: string;
    newsletter?: string;
    newsletterTemplate?: boolean;
    emails?: Destinatario[];
    emailGroups?: NewsletterEmailGroup[];
}
