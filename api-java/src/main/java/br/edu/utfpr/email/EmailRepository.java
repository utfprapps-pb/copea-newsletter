package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends GenericRepository<Email, Long> {

    List<Email> findAllByEmailContaining(String email);

    @Query(value = "SELECT emails " +
            "FROM Newsletter newsletter " +
            "JOIN newsletter.emails emails " +
            "WHERE (emails.email = :email) and (newsletter.id = :newsletterId)")
    List<Email> findByEmailAndNewsletter(String email, Long newsletterId);

    @Query("SELECT e " +
            "FROM EmailGroupRelation g " +
            "LEFT JOIN g.email e " +
            "WHERE (e.email = :email) and (g.emailGroup.id = :groupId)")
    Optional<Email> findByEmailAndGroupId(String email, Long groupId);

    Optional<Email> findByEmail(String email);

    @Query("SELECT e " +
            "FROM EmailGroupRelation g " +
            "LEFT JOIN g.email e " +
            "WHERE (g.emailGroup.id = :groupId)")
    List<Email> findByGroupId(Long groupId);

}
