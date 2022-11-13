package br.edu.utfpr.emailgroup;

import br.edu.utfpr.email.Email;
import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailGroupRepository extends GenericRepository<EmailGroup, Long> {

}
