package br.edu.utfpr.email.send.automated;

import br.edu.utfpr.email.send.automated.schedule.AutomatedScheduler;
import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;

import java.util.Date;

@Getter
@Setter
public class AutomatedSendEmailScheduler implements AutomatedScheduler {

    private Class<? extends Job> jobClass;
    private String jobIdentity;
    private String jobGroup;
    private String triggerIdentity;
    private String triggerGroup;
    private int intervalInHours;
    private Date dateStartAt;
    private Date dateEndAt;
    private boolean recurrent = false;

}