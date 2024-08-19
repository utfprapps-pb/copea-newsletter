package br.edu.utfpr.features.quartz.tasks;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.quartz.tasks.schedule.QuartzTasksScheduleHandle;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.features.quartz.tasks.schedule.QuartzTasksSchedule;
import br.edu.utfpr.features.quartz.tasks.schedule.IQuartzTasksSchedule;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequestScoped
public class QuartzTasksService extends GenericService<QuartzTasks, Long, QuartzTasksRepository> {

    @Inject
    QuartzTasksScheduleHandle quartzTasksScheduleHandle;

    public void scheduleJob(List<QuartzTasks> quartzTasks) {
        if (quartzTasks.isEmpty())
            return;

        List<IQuartzTasksSchedule> quartzTasksSchedules = new ArrayList<>();
        for (QuartzTasks quartzTask : quartzTasks) {
            QuartzTasksSchedule automatedScheduler = new QuartzTasksSchedule();
            setIdentityJobTriggerOnJobDataMap(quartzTask);
            automatedScheduler.setJobDataMap(quartzTask.getJobDataMap());
            automatedScheduler.setJobClass(quartzTask.getJobClass());
            automatedScheduler.setJobIdentity(quartzTask.getJobName());
            automatedScheduler.setJobGroup(quartzTask.getJobGroup());
            automatedScheduler.setTriggerIdentity(quartzTask.getTriggerName());
            automatedScheduler.setTriggerGroup(quartzTask.getTriggerGroup());
            automatedScheduler.setDateStartAt(Date.from(quartzTask.getStartAt().atZone(ZoneId.systemDefault()).toInstant()));
            automatedScheduler.setRecurrent(quartzTask.isRecurrent());
            if (automatedScheduler.isRecurrent()) {
                automatedScheduler.setIntervalInHours(
                        daysToHours(quartzTask.getDayRange())
                );
                automatedScheduler.setDateEndAt(
                        Date.from(quartzTask.getEndAt().atZone(ZoneId.systemDefault()).toInstant())
                );
            }
            quartzTasksSchedules.add(automatedScheduler);
        }

        if (quartzTasksSchedules.isEmpty())
            return;

        try {
            quartzTasksScheduleHandle.schedule(quartzTasksSchedules);
        } catch (Exception exception) {
            throw new ValidationException(
                    "Ocorreu um erro ao agendar as tarefas " +
                            System.lineSeparator() +
                            "Detalhes: " + exception.getMessage());
        }
    }

    private int daysToHours(Integer days) {
        return (days * 24);
    }

    private void setIdentityJobTriggerOnJobDataMap(QuartzTasks quartzTask) {
        JobDataMap jobDataMap = quartzTask.getJobDataMap();
        if (Objects.isNull(jobDataMap))
            jobDataMap = new JobDataMap();
        jobDataMap.put(QuartzTasks.JOB_DATA_JOB_NAME, quartzTask.getJobName());
        jobDataMap.put(QuartzTasks.JOB_DATA_JOB_GROUP, quartzTask.getJobGroup());
        jobDataMap.put(QuartzTasks.JOB_DATA_TRIGGER_NAME, quartzTask.getTriggerName());
        jobDataMap.put(QuartzTasks.JOB_DATA_TRIGGER_GROUP, quartzTask.getTriggerGroup());
    }

    public boolean cancelJob(Long quarzTasksId) throws SchedulerException {
        QuartzTasks quartzTasks = this.findById(quarzTasksId);
        if (Objects.isNull(quartzTasks))
            throw new NotFoundException("Nenhum agendamento encontrado para o id informado.");

        boolean canceled = quartzTasksScheduleHandle.deleteJob(quartzTasks.getJobName(), quartzTasks.getJobGroup());
        if (!canceled)
            throw new ValidationException("O Job de nome " + quartzTasks.getJobName() + " pertencente ao id do agendamento informado não foi encontrado.");

        quartzTasks.setCanceled(true);
        quartzTasks.setCanceledAt(LocalDateTime.now());
        this.save(quartzTasks);
        return true;
    }

    public Optional<QuartzTasks> findByJobNameAndJobGroupAndTriggerNameAndTriggerGroup(
            String jobName, String jobGroup, String triggerName, String triggerGroup
    ) {
        return this.getRepository().findByJobNameAndJobGroupAndTriggerNameAndTriggerGroup(jobName, jobGroup, triggerName, triggerGroup);
    }

}
