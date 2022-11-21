package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends GenericRepository<Email, Long> {

    List<Email> findAllByEmailContaining(String email);

    @Query("SELECT e " +
            "FROM email_group_email g " +
            "LEFT JOIN g.email e " +
            "WHERE g.email_group_id = :groupId")
    List<Email> findByEmailGroupId(Long groupId);

}
