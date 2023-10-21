package br.edu.utfpr.features.user;

import br.edu.utfpr.features.user.recover_password.RecoverPasswordDTO;
import br.edu.utfpr.features.user.responses.ExistsResponse;
import br.edu.utfpr.features.user.responses.SendEmailCodeRecoverPassword;
import br.edu.utfpr.generic.crud.resource.mapstruct.GenericResourceDto;
import br.edu.utfpr.reponses.DefaultResponse;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Objects;

@RequestScoped
@Path("user")
public class UserResource extends GenericResourceDto<
        User,
        UserDTO,
        UserMapper,
        Long,
        UserService> {

    @Path("exists")
    @GET
    public ExistsResponse userExists(@QueryParam("username") String username,
                                     @QueryParam("email") String email) {
        return new ExistsResponse(
                !Objects.isNull(getService().findByUsernameOrEmail(username, email))
        );
    }

    @POST
    @Path("/send-code-recover-password/username/{username}")
    public SendEmailCodeRecoverPassword sendEmailCodeRecoverPassword(@PathParam("username") String username) throws Exception {
        return getService().sendEmailCodeRecoverPassword(username);
    }

    @POST
    @Path("/recover-password")
    public Response recoverPassword(@Valid RecoverPasswordDTO recoverPasswordDTO) {
        DefaultResponse defaultResponse = getService().recoverPassword(recoverPasswordDTO);
        return Response.status(defaultResponse.getHttpStatus()).entity(defaultResponse).build();
    }

}
