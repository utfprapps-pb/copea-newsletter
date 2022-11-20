package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends GenericRepository<Email, Long> {

    List<Email> findAllByEmailContaining(String email);

    //List<Email> findByGroupId(Long groupId);

}
