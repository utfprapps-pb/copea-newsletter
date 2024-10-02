import { EmailGroupRelation } from "../../grupo-destinatarios/model/grupo-destinatario";

export interface Destinatario {
    id?: number;
    email?: string;
    emailGroupRelations?: EmailGroupRelation[],
    subscribed?: string;
    lastEmailUnsubscribedDate?: Date;
    unsubscribeReason?: string;
}
