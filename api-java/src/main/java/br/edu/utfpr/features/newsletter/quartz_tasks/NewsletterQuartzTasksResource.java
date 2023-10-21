package br.edu.utfpr.features.newsletter.quartz_tasks;

import br.edu.utfpr.generic.crud.resource.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("v1/newsletter/quartz-task")
@RequestScoped
public class NewsletterQuartzTasksResource extends GenericResource<NewsletterQuartzTasks, Long, NewsletterQuartzTasksService> {

    @GET
    @Path("active-schedules")
    public Response findActiveSchedules(@QueryParam("newsletter-id") Long newsletterId) {
        return Response.ok(getService().findActiveQuartzTasksByNewsletter(newsletterId)).build();
    }

}