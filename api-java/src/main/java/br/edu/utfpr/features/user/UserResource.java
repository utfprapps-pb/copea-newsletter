package br.edu.utfpr.features.user;

import br.edu.utfpr.generic.crud.resource.GenericResource;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.features.user.recover_password.RecoverPasswordDTO;
import br.edu.utfpr.features.user.responses.ExistsResponse;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Objects;

@RequestScoped
@Path("user")
public class UserResource extends GenericResource<User, Long, UserService> {

    @Path("exists")
    @GET
    public Response userExists(@QueryParam("username") String username,
                               @QueryParam("email") String email) {
        return Response.ok(
                new ExistsResponse(
                        !Objects.isNull(getService().findByUsernameOrEmail(username, email))
                )
        ).build();
    }

    @POST
    @Path("/send-code-recover-password/username/{username}")
    public Response sendEmailCodeRecoverPassword(@PathParam("username") String username) throws Exception {
        return Response.ok(getService().sendEmailCodeRecoverPassword(username)).build();
    }

    @POST
    @Path("/recover-password")
    public Response recoverPassword(@Valid RecoverPasswordDTO recoverPasswordDTO) {
        DefaultResponse defaultResponse = getService().recoverPassword(recoverPasswordDTO);
        return Response.status(defaultResponse.getHttpStatus()).entity(defaultResponse).build();
    }

}
