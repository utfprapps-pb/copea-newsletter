package br.edu.utfpr.newsletter.quartz_tasks.schedule;

import br.edu.utfpr.email.read.ReadEmailService;
import br.edu.utfpr.newsletter.NewsletterService;
import br.edu.utfpr.newsletter.quartz_tasks.NewsletterQuartzTasks;
import br.edu.utfpr.quartz.tasks.QuartzTasks;
import br.edu.utfpr.reponses.DefaultResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import java.util.Objects;
import java.util.logging.Logger;

public class NewsletterQuartzTasksScheduleJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(ReadEmailService.class.getName());

    @Inject
    NewsletterService newsletterService;

    @Override
    @ActivateRequestContext
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String newsletterId = jobDataMap.getString(NewsletterQuartzTasks.JOB_DATA_NEWSLETTER_ID);
        if (Objects.isNull(newsletterId) || newsletterId.isEmpty())
            return;

        try {
            DefaultResponse defaultResponse = newsletterService.sendScheduledNewsletterByEmail(
                    Long.valueOf(newsletterId),
                    jobDataMap.getString(QuartzTasks.JOB_DATA_JOB_NAME),
                    jobDataMap.getString(QuartzTasks.JOB_DATA_JOB_GROUP),
                    jobDataMap.getString(QuartzTasks.JOB_DATA_TRIGGER_NAME),
                    jobDataMap.getString(QuartzTasks.JOB_DATA_TRIGGER_GROUP)
            );
            if (Objects.equals(defaultResponse.getHttpStatus(), RestResponse.StatusCode.OK))
                LOGGER.info("Tarefa agendada do envio de e-mail das newsletters pelo Quartz: " + defaultResponse.getMessage());
            else
                LOGGER.severe("Problema ao enviar e-mail da newsletter pela tarefa agendada do Quartz: " + defaultResponse.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Erro ao executar tarefa agendada do envio de e-mail das newsletters pelo Quartz: " + e.getMessage());
            e.printStackTrace();
        }
    }

}