package br.edu.utfpr.exception;

import br.edu.utfpr.reponses.GenericResponse;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(GenericResponse
                        .getGenericResponse(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode()))
                .build();
    }
}
