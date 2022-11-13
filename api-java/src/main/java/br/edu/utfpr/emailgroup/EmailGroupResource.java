package br.edu.utfpr.emailgroup;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("v1/email-group")
@RequestScoped
public class EmailGroupResource extends GenericResource<EmailGroup, Long, EmailGroupService> {

}