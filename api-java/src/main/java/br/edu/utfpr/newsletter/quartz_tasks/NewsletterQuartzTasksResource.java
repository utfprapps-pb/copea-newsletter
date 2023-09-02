package br.edu.utfpr.newsletter.quartz_tasks;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("v1/newsletter/quartz-task")
@RequestScoped
public class NewsletterQuartzTasksResource extends GenericResource<NewsletterQuartzTasks, NewsletterQuartzTasks, Long, NewsletterQuartzTasksService> {

    public NewsletterQuartzTasksResource() {
        super(NewsletterQuartzTasks.class, NewsletterQuartzTasks.class);
    }

}