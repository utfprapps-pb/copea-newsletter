package br.edu.utfpr.email.send.automated.schedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class AutomatedSchedule {

    @Inject
    Scheduler scheduler;

    public void schedule(List<AutomatedScheduler> automatedSchedulers) throws SchedulerException {
        if (automatedSchedulers.isEmpty())
            return;

        for (AutomatedScheduler automatedScheduler : automatedSchedulers) {
            scheduler.scheduleJob(automatedScheduler.getJobDetail(), automatedScheduler.getTrigger());
        }
    }

}