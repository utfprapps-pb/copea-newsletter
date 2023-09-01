package br.edu.utfpr.teste;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobTest implements Job {

    // Chama esse método toda vez que o Job é executado pelo scheduler
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Tarefa executada!");
    }

}