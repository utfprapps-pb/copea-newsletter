package br.edu.utfpr.observers;

import br.edu.utfpr.quartz.tasks.schedule.QuartzTasksSchedule;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppStartup {

    @Inject
    QuartzTasksSchedule quartzTasksSchedule;

    void onStart(@Observes StartupEvent startupEvent) {
//        AutomatedSendEmailScheduler automatedScheduler = new AutomatedSendEmailScheduler();
//        automatedScheduler.setJobClass(AutomatedSendEmailJob.class);
//        automatedScheduler.setJobIdentity("teste");
//        automatedScheduler.setJobGroup("teste");
//        automatedScheduler.setTriggerIdentity("teste");
//        automatedScheduler.setTriggerGroup("teste");
//        automatedScheduler.setIntervalInMinutes(1);
//        automatedScheduler.setDateStartAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
//        automatedScheduler.setDateEndAt(
//                Date.from(LocalDateTime.now()
////                            .plusMinutes(4)
//                        .plusMinutes(4)
//                        .atZone(ZoneId.systemDefault()).toInstant())
//        );
//        automatedScheduler.setRecurrent(true);
//
//        AutomatedSendEmailScheduler automatedScheduler1 = new AutomatedSendEmailScheduler();
//        automatedScheduler1.setJobClass(AutomatedSendEmailJob1.class);
//        automatedScheduler1.setJobIdentity("job_1");
//        automatedScheduler1.setJobGroup("teste");
//        automatedScheduler1.setTriggerIdentity("trigger_1");
//        automatedScheduler1.setTriggerGroup("teste");
//        automatedScheduler1.setIntervalInMinutes(1);
//        automatedScheduler1.setDateStartAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
//        automatedScheduler1.setDateEndAt(
//                Date.from(LocalDateTime.now()
//                            .plusMinutes(4)
//                        .plusMinutes(16)
//                        .atZone(ZoneId.systemDefault()).toInstant())
//        );
//        automatedScheduler1.setRecurrent(true);
//
//        List<AutomatedScheduler> automatedSchedulers = new ArrayList<>();
//        automatedSchedulers.add(automatedScheduler);
//        automatedSchedulers.add(automatedScheduler1);
//        automatedSchedule.schedule(automatedSchedulers);
    }

}