import { Destinatario } from "src/app/pages/destinatarios/model/destinatario";

export interface EmailGroupRelation {
  id?: number;
  email?: Destinatario;
  emailGroup?: GrupoDestinatario;
  uuidWasSelfRegistration?: string;
}

export interface GrupoDestinatario {
    id?: number;
    name?: string;
    uuidToSelfRegistration?: string;
}
