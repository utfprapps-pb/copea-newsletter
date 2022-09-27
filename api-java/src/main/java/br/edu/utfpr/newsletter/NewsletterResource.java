package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("v1/newsletter")
@RequestScoped
public class NewsletterResource extends GenericResource<Newsletter, Long, NewsletterService> {

}
