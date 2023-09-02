package br.edu.utfpr.newsletter.quartz_tasks;

import br.edu.utfpr.email.send.automated.AutomatedSendEmailScheduler;
import br.edu.utfpr.email.send.automated.QuartzTasks;
import br.edu.utfpr.email.send.automated.schedule.AutomatedSchedule;
import br.edu.utfpr.email.send.automated.schedule.AutomatedScheduler;
import br.edu.utfpr.email.send.automated.schedule.AutomatedSendEmailJob;
import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestScoped
public class NewsletterQuartzTasksService extends GenericService<NewsletterQuartzTasks, Long, NewsletterQuartzTasksRepository> {

    @Inject
    AutomatedSchedule automatedSchedule;

    @Override
    public void setDefaultValuesWhenNew(NewsletterQuartzTasks entity) {
        setDefaultValues(entity);
        configureJobAndTriggerOnQuartzTask(entity);
    }

    private void setDefaultValues(NewsletterQuartzTasks requestBody) {
        requestBody.getQuartzTask().setCreatedAt(LocalDateTime.now());
    }

    private void configureJobAndTriggerOnQuartzTask(NewsletterQuartzTasks requestBody) {
        QuartzTasks quartzTask = requestBody.getQuartzTask();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String name = "newsletter_" + requestBody.getNewsletter().getId() + "_start_at_" + quartzTask.getStartAt().format(formatter);
        quartzTask.setJobName("job_" + name);
        quartzTask.setJobGroup("newsletter_email_tasks");
        quartzTask.setTriggerName("trigger_" + name);
        quartzTask.setTriggerGroup("newsletter_email_tasks");

        AutomatedSendEmailScheduler automatedScheduler = new AutomatedSendEmailScheduler();
        automatedScheduler.setJobClass(AutomatedSendEmailJob.class);
        automatedScheduler.setJobIdentity(quartzTask.getJobName());
        automatedScheduler.setJobGroup(quartzTask.getJobGroup());
        automatedScheduler.setTriggerIdentity(quartzTask.getTriggerName());
        automatedScheduler.setTriggerGroup(quartzTask.getTriggerGroup());
        automatedScheduler.setDateStartAt(Date.from(quartzTask.getStartAt().atZone(ZoneId.systemDefault()).toInstant()));
        automatedScheduler.setRecurrent(quartzTask.isRecurrent());
        if (automatedScheduler.isRecurrent()) {
//            automatedScheduler.setIntervalInHours(quartzTask.getDayRange() * 24);
            automatedScheduler.setIntervalInHours(quartzTask.getDayRange()); // teste pois por enquanto no AutomatedScheduler está por minuto para testar
            automatedScheduler.setDateEndAt(
                    Date.from(quartzTask.getEndAt().atZone(ZoneId.systemDefault()).toInstant())
            );
        }
        List<AutomatedScheduler> automatedSchedulers = new ArrayList<>();
        automatedSchedulers.add(automatedScheduler);

        try {
            automatedSchedule.schedule(automatedSchedulers);
        } catch (Exception exception) {
            throw new ValidationException(
                    "Ocorreu um erro ao tentar agendar o envio da newsletter por e-mail." +
                            System.lineSeparator() +
                            "Detalhes: " + exception.getMessage());
        }
    }

}