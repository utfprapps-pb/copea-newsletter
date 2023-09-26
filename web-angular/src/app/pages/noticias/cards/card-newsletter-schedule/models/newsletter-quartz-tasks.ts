import { QuartzTasks } from "src/app/pages/noticias/cards/card-newsletter-schedule/models/quartz-tasks";
import { Noticia } from "src/app/pages/noticias/models/noticia";

export interface NewsletterQuartzTasks {
  id?: number;
  newsletter: Noticia;
  quartzTask: QuartzTasks;
}
