package br.edu.utfpr.features.quartz.tasks;

import br.edu.utfpr.generic.crud.resource.GenericResource;
import br.edu.utfpr.reponses.DefaultBooleanResponse;
import org.quartz.SchedulerException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("v1/quartz-task")
@RequestScoped
public class QuartzTasksResource extends GenericResource<QuartzTasks, Long, QuartzTasksService> {

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
