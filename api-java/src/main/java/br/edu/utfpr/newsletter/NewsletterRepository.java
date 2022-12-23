package br.edu.utfpr.newsletter;

import br.edu.utfpr.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.generic.crud.GenericRepository;
import br.edu.utfpr.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsletterRepository extends GenericRepository<Newsletter, Long> {

    Optional<Newsletter> findById(Long id);

    Optional<Newsletter> findByIdAndUser(Long id, User user);

    List<Newsletter> findByUser(User user);

    @Query("select distinct n from Newsletter n left join n.sendEmailLogs ns where (n.user.id = :userId) and (ns.sentStatus = :sentStatus) and (n.newsletterTemplate = :newsletterTemplate) and (n.description like '%' || :description || '%')")
    List<Newsletter> findBySentStatusAndNewsletterTemplateAndDescription(
            @Param("sentStatus") SendEmailLogStatusEnum sentStatus,
            @Param("newsletterTemplate") Boolean newsletterTemplate,
            @Param("description") String description,
            @Param("userId") Long userId
    );

    @Query("select distinct n from Newsletter n left join n.sendEmailLogs ns where (n.user.id = :userId) and ((ns.sentStatus is null) or (ns.sentStatus <> :sentStatus)) and (n.newsletterTemplate = :newsletterTemplate) and (n.description like '%' || :description || '%')")
    List<Newsletter> findBySentStatusIsNotAndNewsletterTemplateAndDescription(
            @Param("sentStatus") SendEmailLogStatusEnum sentStatus,
            @Param("newsletterTemplate") Boolean newsletterTemplate,
            @Param("description") String description,
            @Param("userId") Long userId
    );

    List<Newsletter> findByNewsletterTemplateAndDescriptionContainsAndUser(Boolean newsletterTemplate, String description, User user);



}
