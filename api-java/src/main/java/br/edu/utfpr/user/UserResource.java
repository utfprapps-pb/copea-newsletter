package br.edu.utfpr.user;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@RequestScoped
@Path("user")
public class UserResource extends GenericResource<User, Long, UserService> {

}
