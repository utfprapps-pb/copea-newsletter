package br.edu.utfpr.features.newsletter.quartz_tasks;

import br.edu.utfpr.generic.crud.resource.GenericResource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("v1/newsletter/quartz-task")
@RequestScoped
public class NewsletterQuartzTasksResource extends GenericResource<NewsletterQuartzTasks, Long, NewsletterQuartzTasksService> {

    @GET
    @Path("active-schedules")
    public Response findActiveSchedules(@QueryParam("newsletter-id") Long newsletterId) {
        return Response.ok(getService().findActiveQuartzTasksByNewsletter(newsletterId)).build();
    }

}