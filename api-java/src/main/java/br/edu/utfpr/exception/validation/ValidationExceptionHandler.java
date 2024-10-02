package br.edu.utfpr.exception.validation;

import br.edu.utfpr.reponses.GenericErrorResponse;
import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(GenericErrorResponse.getGenericResponse(
                        exception.getMessage(),
                        Response.Status.BAD_REQUEST.getStatusCode()))
                .build();
    }

}
