package br.edu.utfpr.teste;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequestScoped
public class QuartzInitializer {

    @Inject
    EntityManager entityManager;

    public void init() {
        try {
            // Inicia o scheduler
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

//            List<Agendamento> agendamentos = entityManager.createQuery("FROM Agendamento", Agendamento.class).getResultList();

//            for (Agendamento agendamento : agendamentos) {

            // Cria o Job com base na minha classe de Job que implementa a interface Job
            JobDetail job = JobBuilder.newJob(JobTest.class)
                    .withIdentity("job" + 1, "group1")
                    .build();

            // Cria a trigger para executar o job a cada tempo
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger" + 1, "group1")
                    .startAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                                .withIntervalInHours(1 * 24)  // dia * 24 hrs
//                            .withIntervalInMinutes(1)  // teste
                            .withIntervalInSeconds(1) // teste
                            .repeatForever())      // repetir indefinidamente
                    .endAt(Date.from(LocalDateTime.now()
//                            .plusMinutes(4)
                            .plusSeconds(10)
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .build();

            scheduler.scheduleJob(job, trigger);
//            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}