package br.edu.utfpr.user;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.user.dtos.UserDTO;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@RequestScoped
@Path("user")
public class UserResource extends GenericResource<User, UserDTO, Long, UserService> {

    public UserResource() {
        super(User.class, UserDTO.class);
    }

}
