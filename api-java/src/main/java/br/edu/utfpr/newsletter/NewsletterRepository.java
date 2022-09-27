package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsletterRepository extends GenericRepository<Newsletter, Long> {

    Optional<Newsletter> findById(Long id);

}
