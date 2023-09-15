package br.edu.utfpr.quartz.tasks;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.reponses.DefaultBooleanResponse;
import org.quartz.SchedulerException;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("v1/quartz-task")
@RequestScoped
public class QuartzTasksResource extends GenericResource<QuartzTasks, QuartzTasks, Long, QuartzTasksService> {

    public QuartzTasksResource() {
        super(QuartzTasks.class, QuartzTasks.class);
    }

    @Transactional
    @DELETE
    @Path("cancel/{id}")
    public Response cancelQuartzTask(@PathParam("id") Long quarzTasksId) throws SchedulerException {
        DefaultBooleanResponse defaultBooleanResponse = new DefaultBooleanResponse();
        defaultBooleanResponse.setValue(
                this.getService().cancelJob(quarzTasksId)
        );
        if (defaultBooleanResponse.isValue())
            defaultBooleanResponse.setMessage("Agendamento cancelado com sucesso.");
        return Response.ok(defaultBooleanResponse).build();
    }

}
