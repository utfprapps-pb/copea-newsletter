package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericRepository;
import br.edu.utfpr.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsletterRepository extends GenericRepository<Newsletter, Long> {

    Optional<Newsletter> findById(Long id);

    Optional<Newsletter> findByIdAndUser(Long id, User user);

    List<Newsletter> findByUser(User user);

}
