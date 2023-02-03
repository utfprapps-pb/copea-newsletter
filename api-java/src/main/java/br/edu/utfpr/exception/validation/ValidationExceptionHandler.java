package br.edu.utfpr.exception.validation;

import br.edu.utfpr.reponses.GenericResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(GenericResponse.getGenericResponse(
                        exception.getMessage(),
                        Response.Status.BAD_REQUEST.getStatusCode()))
                .build();
    }

}
