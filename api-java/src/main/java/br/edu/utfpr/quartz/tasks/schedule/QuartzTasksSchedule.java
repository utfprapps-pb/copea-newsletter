package br.edu.utfpr.quartz.tasks.schedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class QuartzTasksSchedule {

    @Inject
    Scheduler scheduler;

    public void schedule(List<IQuartzTasksSchedule> quartzTasksSchedules) throws SchedulerException {
        if (quartzTasksSchedules.isEmpty())
            return;

        for (IQuartzTasksSchedule quartzTasksSchedule : quartzTasksSchedules) {
            scheduler.scheduleJob(quartzTasksSchedule.getJobDetail(), quartzTasksSchedule.getTrigger());
        }
    }

}