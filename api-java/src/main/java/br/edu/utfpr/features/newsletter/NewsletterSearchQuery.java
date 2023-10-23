package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.auth.AuthSecurityFilter;
import br.edu.utfpr.features.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.features.newsletter.requests.NewsletterSearchRequest;
import br.edu.utfpr.features.user.User;
import br.edu.utfpr.sql.builder.SqlBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class NewsletterSearchQuery {

    @Inject
    EntityManager entityManager;

    @Inject
    AuthSecurityFilter authSecurityFilter;

    public List<Newsletter> search(NewsletterSearchRequest newsletterSearchRequest) {
        SqlBuilder sqlBuilder = new SqlBuilder("select newsletter.* from newsletter");
        if ((Objects.nonNull(newsletterSearchRequest.getDescription())) && (!newsletterSearchRequest.getDescription().isEmpty()))
            sqlBuilder.addAnd("(newsletter.description ilike '%' || :description || '%')", "description", newsletterSearchRequest.getDescription());
        addFilterNewsletterSentOrNot(sqlBuilder, newsletterSearchRequest);
        addFilterNewslettersTemplateMineOrShared(sqlBuilder, newsletterSearchRequest);
        Query query = sqlBuilder.createNativeQuery(entityManager, Newsletter.class);
        return query.getResultList();
    }

    private void addFilterNewsletterSentOrNot(SqlBuilder sqlBuilder, NewsletterSearchRequest newsletterSearchRequest) {
        if (newsletterSearchRequest.isNewslettersSent() == newsletterSearchRequest.isNewslettersNotSent())
            return;

        final String NEWSLETTER_SENT_FILTER = "(exists(select 1 from newsletter_send_email_log " +
                "left join send_email_log on (send_email_log.id = newsletter_send_email_log.send_email_log_id) " +
                "where (newsletter_send_email_log.newsletter_id = newsletter.id) and send_email_log.sent_status = :sentStatusSent))";

        if (newsletterSearchRequest.isNewslettersSent())
            sqlBuilder.addAnd(NEWSLETTER_SENT_FILTER, "sentStatusSent", SendEmailLogStatusEnum.SENT.name());

        if (newsletterSearchRequest.isNewslettersNotSent())
            sqlBuilder.addAnd("(not " + NEWSLETTER_SENT_FILTER + ")", "sentStatusSent", SendEmailLogStatusEnum.SENT.name());
    }

    private void addFilterNewslettersTemplateMineOrShared(SqlBuilder sqlBuilder, NewsletterSearchRequest newsletterSearchRequest) {
        User loggedUser = authSecurityFilter.getAuthUserContext().findByToken();

        final String PARAM_LOGGED_USER_ID = "loggedUserId";

        if (Objects.equals(newsletterSearchRequest.isNewslettersTemplateMine(), newsletterSearchRequest.isNewslettersTemplateShared())) {
            boolean bothChecked = newsletterSearchRequest.isNewslettersTemplateMine();
            if (bothChecked)
                sqlBuilder.addAnd("(newsletter.newsletter_template)");
            else {
                sqlBuilder.addAnd("(not (newsletter.newsletter_template))");
                sqlBuilder.addAnd(
                        "(newsletter.user_id = :" + PARAM_LOGGED_USER_ID + ")",
                        PARAM_LOGGED_USER_ID,
                        loggedUser.getId()
                );
            }
            return;
        }
        if (!newsletterSearchRequest.isNewslettersTemplateMine())
            sqlBuilder.addAnd(
                    "(newsletter.newsletter_template) and (newsletter.user_id <> :" + PARAM_LOGGED_USER_ID + ")",
                    PARAM_LOGGED_USER_ID,
                    loggedUser.getId()
            );
        if (!newsletterSearchRequest.isNewslettersTemplateShared())
            sqlBuilder.addAnd(
                    "(newsletter.newsletter_template) and (newsletter.user_id = :" + PARAM_LOGGED_USER_ID + ")",
                    PARAM_LOGGED_USER_ID,
                    loggedUser.getId()
            );
    }

}
