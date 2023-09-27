import { GrupoDestinatario } from "src/app/pages/grupo-destinatarios/model/grupo-destinatario";
import { Noticia } from "src/app/pages/noticias/models/noticia";

export interface NewsletterEmailGroup {
  id?: number;
  newsletter?: Noticia;
  emailGroup?: GrupoDestinatario;
}
