package br.edu.utfpr.quartz.tasks.schedule;

import org.quartz.*;

import java.util.Date;

public interface IQuartzTasksSchedule {

    Class<? extends Job> getJobClass();
    void setJobClass(Class<? extends Job> jobClass);

    String getJobIdentity();
    void setJobIdentity(String jobIdentity);

    String getJobGroup();
    void setJobGroup(String jobGroup);

    String getTriggerIdentity();
    void setTriggerIdentity(String triggerIdentity);

    String getTriggerGroup();
    void setTriggerGroup(String triggerGroup);

    int getIntervalInHours();
    void setIntervalInHours(int intervalInHours);

    Date getDateStartAt();
    void setDateStartAt(Date dateStartAt);

    Date getDateEndAt();
    void setDateEndAt(Date dateEndAt);

    boolean isRecurrent();
    void setRecurrent(boolean recurrent);

    JobDataMap getJobDataMap();
    void setJobDataMap(JobDataMap jobDataMap);

    default JobDetail getJobDetail() {
        return JobBuilder.newJob(getJobClass())
                .withIdentity(getJobIdentity(), getJobGroup())
                .build();
    }

    default Trigger getTrigger() {
        if (isRecurrent())
            return TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerIdentity(), getTriggerGroup())
                    .startAt(getDateStartAt())
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInHours(getIntervalInHours())
                                    .repeatForever()
                    )
                    .endAt(getDateEndAt())
                    .build();

        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerIdentity(), getTriggerGroup())
                .startAt(getDateStartAt())
                .build();
    }

}
