package br.edu.utfpr.features.email.group;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailGroupRepository extends GenericRepository<EmailGroup, Long> {

    Optional<EmailGroup> findByUuidToSelfRegistration(String uuidToSelfRegistration);

    Optional<EmailGroup> findByName(String name);

}
