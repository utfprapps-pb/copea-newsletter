package br.edu.utfpr.user;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.user.dtos.UserDTO;
import br.edu.utfpr.user.responses.UserExists;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Objects;

@RequestScoped
@Path("user")
public class UserResource extends GenericResource<User, UserDTO, Long, UserService> {

    public UserResource() {
        super(User.class, UserDTO.class);
    }

    @Path("exists")
    @GET
    public Response userExists(@QueryParam("username") String username,
                               @QueryParam("email") String email) {
        return Response.ok(
                new UserExists(
                        !Objects.isNull(getService().findByUsernameOrEmail(username, email))
                )
        ).build();
    }

    @POST
    @Path("/send-code-recover-password/username/{username}")
    public Response sendEmailCodeRecoverPassword(@PathParam("username") String username) throws Exception {
        return Response.ok(getService().sendEmailCodeRecoverPassword(username)).build();
    }

}
