package br.edu.utfpr.newsletter.quartz_tasks.schedule;

import br.edu.utfpr.quartz.tasks.schedule.IQuartzTasksSchedule;
import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.Date;

@Getter
@Setter
public class NewsletterQuartzTasksSchedule implements IQuartzTasksSchedule {

    private Class<? extends Job> jobClass;
    private String jobIdentity;
    private String jobGroup;
    private String triggerIdentity;
    private String triggerGroup;
    private int intervalInHours;
    private Date dateStartAt;
    private Date dateEndAt;
    private boolean recurrent = false;
//    private Long newsletterId;
    private JobDataMap jobDataMap;

    @Override
    public JobDetail getJobDetail() {
        return JobBuilder.newJob(getJobClass())
                .withIdentity(getJobIdentity(), getJobGroup())
//                .usingJobData(JOB_DATA_NEWSLETTER_ID, String.valueOf(getNewsletterId()))
                .usingJobData(getJobDataMap())
                .build();
    }
}