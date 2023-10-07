package br.edu.utfpr.quartz.tasks.schedule;

import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.Date;

@Getter
@Setter
public class QuartzTasksSchedule implements IQuartzTasksSchedule {

    private Class<? extends Job> jobClass;
    private String jobIdentity;
    private String jobGroup;
    private String triggerIdentity;
    private String triggerGroup;
    private int intervalInHours;
    private Date dateStartAt;
    private Date dateEndAt;
    private boolean recurrent = false;
    private JobDataMap jobDataMap;

    @Override
    public JobDetail getJobDetail() {
        return JobBuilder.newJob(getJobClass())
                .withIdentity(getJobIdentity(), getJobGroup())
                .usingJobData(getJobDataMap())
                .build();
    }
}