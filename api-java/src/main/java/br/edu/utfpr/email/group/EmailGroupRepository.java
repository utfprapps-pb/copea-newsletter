package br.edu.utfpr.email.group;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailGroupRepository extends GenericRepository<EmailGroup, Long> {

    Optional<EmailGroup> findByUuidToSelfRegistration(String uuidToSelfRegistration);

}
