package br.edu.utfpr.features.newsletter.quartz_tasks;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.newsletter.Newsletter;
import br.edu.utfpr.features.newsletter.quartz_tasks.schedule.NewsletterQuartzTasksScheduleJob;
import br.edu.utfpr.features.quartz.tasks.QuartzTasks;
import br.edu.utfpr.features.quartz.tasks.QuartzTasksService;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.sql.builder.SqlBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.quartz.JobDataMap;

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
        validScheduleDates(quartzTask);

        String name = getUniqueNameTask(newsletter.getId(), quartzTask.getStartAt());
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

    private String getUniqueNameTask(Long newsletterId, LocalDateTime startAt) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmssMs");
        return new StringBuilder()
                .append("newsletter_")
                .append(newsletterId)
                .append("_start_")
                .append(startAt.format(dateTimeFormatter))
                .append("_created_")
                .append(LocalDateTime.now().format(dateTimeFormatter))
                .toString();
    }

    private void validJustOneActiveQuartzTasksByNewsletter(Long newsletterId) {
        List<QuartzTasks> quartzTasks = findActiveQuartzTasksByNewsletter(newsletterId);
        if (!quartzTasks.isEmpty())
            throw new ValidationException(
                    "Já existe um agendamento ativo para a notícia informada. " +
                            "Cancele o atual para incluir um novo."
            );
    }

    private void validScheduleDates(QuartzTasks quartzTask) {
        if (Objects.nonNull(quartzTask.getStartAt()) && quartzTask.getStartAt().isBefore(LocalDateTime.now()))
            throw new ValidationException("A data e hora de início não pode ser inferior à data e hora atual.");

        if (Objects.nonNull(quartzTask.getEndAt()) && quartzTask.getEndAt().isBefore(quartzTask.getStartAt()))
            throw new ValidationException("A data e hora de encerramento não pode ser inferior à data e hora de início.");
    }

    public List<QuartzTasks> findActiveQuartzTasksByNewsletter(Long newsletterId) {
        /**
         * TODO: Jogar execução desse SQL e os demais no repository, porém precisa encontrar
         * uma forma de deixar o repository como uma classe e não interface,
         * o repository do spring no quarkus não suporta @Query nativa
         */
        SqlBuilder sqlBuilder = new SqlBuilder(
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