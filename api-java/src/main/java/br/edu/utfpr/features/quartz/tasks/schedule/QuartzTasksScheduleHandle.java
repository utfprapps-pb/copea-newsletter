package br.edu.utfpr.features.quartz.tasks.schedule;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;

@RequestScoped
public class QuartzTasksScheduleHandle {

    @Inject
    Scheduler scheduler;

    public void schedule(List<IQuartzTasksSchedule> quartzTasksSchedules) throws SchedulerException {
        if (quartzTasksSchedules.isEmpty())
            return;

        for (IQuartzTasksSchedule quartzTasksSchedule : quartzTasksSchedules) {
            scheduler.scheduleJob(quartzTasksSchedule.getJobDetail(), quartzTasksSchedule.getTrigger());
        }
    }

    public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.deleteJob(new JobKey(jobName, jobGroup));
    }

}