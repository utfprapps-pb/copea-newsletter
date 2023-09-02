package br.edu.utfpr.email.send.automated.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutomatedSendEmailJob implements Job {

    // Chama esse método toda vez que o Job é executado pelo scheduler
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Tarefa executada!");
    }

}