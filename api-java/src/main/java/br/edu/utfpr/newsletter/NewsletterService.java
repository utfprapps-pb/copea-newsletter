package br.edu.utfpr.newsletter;

import br.edu.utfpr.email.send.SendEmailService;
import br.edu.utfpr.email.send.log.SendEmailLog;
import br.edu.utfpr.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class NewsletterService extends GenericService<Newsletter, Long, NewsletterRepository> {

    @Inject
    SendEmailService sendEmailService;

    @Override
    public GenericResponse save(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public GenericResponse update(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    private GenericResponse saveOrUpdate(Newsletter entity) {
        setDefaultValues(entity);
        return super.save(entity);
    }

    private void setDatesByNewOrUpdate(Newsletter entity) {
        if ((!Objects.isNull(entity.getId())) && (getRepository().existsById(entity.getId())))
            entity.setAlterationDate(LocalDateTime.now());
        else
            entity.setInclusionDate(LocalDateTime.now());
    }
    
    private void setDefaultValues(Newsletter entity) {
        setDatesByNewOrUpdate(entity);
        if (Objects.isNull(entity.getUser()))
            entity.setUser(getAuthSecurityFilter().getAuthUserContext().findByToken());
    }

    public DefaultResponse sendNewsletterByEmail(Long newsletterId) throws Exception {
        Optional<Newsletter> optionalNewsletterEntity = getRepository().findByIdAndUser(newsletterId, getAuthSecurityFilter().getAuthUserContext().findByToken());
        if (!optionalNewsletterEntity.isPresent())
            return new DefaultResponse().builder()
                    .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                    .message("Nenhuma newsletter encontrada para o parâmetro informado.")
                    .build();

        Newsletter newsletterEntity = optionalNewsletterEntity.get();

        SendEmailLog sendEmailLog = sendEmailService.send(
                newsletterEntity.getSubject(),
                newsletterEntity.getNewsletter(),
                sendEmailService.convertArrayEmailEntityToStringArray(newsletterEntity.getEmails()));

        newsletterEntity.getSendEmailLogs().add(sendEmailLog);
        getRepository().save(newsletterEntity);

        if (SendEmailLogStatusEnum.SENT.equals(sendEmailLog.getSentStatus()))
            return new DefaultResponse().builder()
                    .httpStatus(RestResponse.StatusCode.OK)
                    .message("Newsletter enviada aos emails vinculados com sucesso.")
                    .build();

        return new DefaultResponse().builder()
                .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                .message(sendEmailLog.getError())
                .build();

    }

    public List<Newsletter> findByUser() {
        List<Newsletter> newsletterList =
                getRepository().findByUser(getAuthSecurityFilter().getAuthUserContext().findByToken());

        if (newsletterList.isEmpty())
            throwNotFoundException();

        return newsletterList;
    }

    public Newsletter findByIdAndUser(Long id) {
        Optional<Newsletter> newsletterOptional =
                getRepository().findByIdAndUser(id, getAuthSecurityFilter().getAuthUserContext().findByToken());

        if (newsletterOptional.isEmpty())
            throwNotFoundException();

        return newsletterOptional.get();
    }

}
