import { GrupoDestinatario } from "../../grupo-destinatarios/model/grupo-destinatario";

export interface Destinatario {
    id?: number;
    email?: string;
    groups?: GrupoDestinatario[],
    subscribed?: string;
    lastEmailUnsubscribedDate?: Date;
}
