package br.edu.utfpr.newsletter.quartz_tasks;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.newsletter.Newsletter;
import br.edu.utfpr.newsletter.quartz_tasks.schedule.NewsletterQuartzTasksScheduleJob;
import br.edu.utfpr.quartz.tasks.QuartzTasks;
import br.edu.utfpr.quartz.tasks.QuartzTasksService;
import br.edu.utfpr.sql.SQLBuilder;
import org.quartz.JobDataMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class NewsletterQuartzTasksService extends GenericService<NewsletterQuartzTasks, Long, NewsletterQuartzTasksRepository> {

    @Inject
    QuartzTasksService quartzTasksService;

    @Inject
    EntityManager entityManager;

    @Override
    public void setDefaultValuesWhenNew(NewsletterQuartzTasks entity) {
        setDefaultValues(entity);
        scheduleJobOnQuartzTask(entity);
    }

    private void setDefaultValues(NewsletterQuartzTasks requestBody) {
        requestBody.getQuartzTask().setCreatedAt(LocalDateTime.now());
    }

    private void scheduleJobOnQuartzTask(NewsletterQuartzTasks requestBody) {
        QuartzTasks quartzTask = requestBody.getQuartzTask();
        Newsletter newsletter = requestBody.getNewsletter();

        validJustOneActiveQuartzTasksByNewsletter(newsletter.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String name = "newsletter_" + newsletter.getId() + "_start_at_" + quartzTask.getStartAt().format(formatter);
        quartzTask.setJobName("job_" + name);
        quartzTask.setJobGroup("newsletter_email_tasks");
        quartzTask.setTriggerName("trigger_" + name);
        quartzTask.setTriggerGroup("newsletter_email_tasks");
        quartzTask.setJobClass(NewsletterQuartzTasksScheduleJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(NewsletterQuartzTasks.JOB_DATA_NEWSLETTER_ID, newsletter.getId().toString());
        quartzTask.setJobDataMap(jobDataMap);

        List<QuartzTasks> quartzTasks = new ArrayList<>();
        quartzTasks.add(quartzTask);
        quartzTasksService.scheduleJob(quartzTasks);
    }

    private void validJustOneActiveQuartzTasksByNewsletter(Long newsletterId) {
        List<QuartzTasks> quartzTasks = findActiveQuartzTasksByNewsletter(newsletterId);
        if (!quartzTasks.isEmpty())
            throw new ValidationException(
                    "Já existe um agendamento ativo para a notícia informada. " +
                            "Cancele o atual para incluir um novo."
            );
    }


    public List<QuartzTasks> findActiveQuartzTasksByNewsletter(Long newsletterId) {
        /**
         * TODO: Jogar execução desse SQL e os demais no repository, porém precisa encontrar
         * uma forma de deixar o repository como uma classe e não interface,
         * o repository do spring no quarkus não suporta @Query nativa
         */
        SQLBuilder sqlBuilder = new SQLBuilder(
                """
            select qrtz_tasks.* from newsletter
            inner join newsletter_qrtz_tasks nqt on (nqt.newsletter_id = newsletter.id)
            inner join qrtz_tasks on (qrtz_tasks.id = nqt.qrtz_tasks_id)
            inner join qrtz_triggers on (qrtz_triggers.trigger_group = qrtz_tasks.trigger_group) and
            (qrtz_triggers.trigger_name = qrtz_tasks.trigger_name)            
            """
        );
        if (Objects.nonNull(newsletterId))
            sqlBuilder.addAnd("(newsletter.id = :newsletterId)", "newsletterId", newsletterId);
        Query query = sqlBuilder.createNativeQuery(entityManager, QuartzTasks.class);
        return query.getResultList();

    }

}