package br.edu.utfpr.email.email;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends GenericRepository<Email, Long> {

    List<Email> findAllByEmailContaining(String email);

}
