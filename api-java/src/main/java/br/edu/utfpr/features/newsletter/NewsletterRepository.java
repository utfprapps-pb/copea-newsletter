package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.generic.crud.GenericRepository;
import br.edu.utfpr.features.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsletterRepository extends GenericRepository<Newsletter, Long> {

    Optional<Newsletter> findById(Long id);

    Optional<Newsletter> findByIdAndUser(Long id, User user);

    List<Newsletter> findByUser(User user);

    @Query("select max(send_email_logs.logDate) from Newsletter newsletter join newsletter.sendEmailLogs send_email_logs where (send_email_logs.sentStatus = 'SENT') and (newsletter.id = :newsletterId)")
    LocalDateTime findLastSentEmail(Long newsletterId);

}
