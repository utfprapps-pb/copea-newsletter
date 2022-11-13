package br.edu.utfpr.emailgroup;

import br.edu.utfpr.generic.crud.GenericService;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class EmailGroupService extends GenericService<EmailGroup, Long, EmailGroupRepository> {

}
