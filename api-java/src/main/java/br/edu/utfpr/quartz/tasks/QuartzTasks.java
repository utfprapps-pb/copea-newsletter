package br.edu.utfpr.quartz.tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "qrtz_tasks")
public class QuartzTasks {

    @Id
    @SequenceGenerator(name = "qrtz_tasks_id_sequence", sequenceName = "qrtz_tasks_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "qrtz_tasks_id_sequence")
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "start_at")
    @NotNull
    private LocalDateTime startAt;

    private boolean recurrent;

    @Column(name = "day_range")
    private Integer dayRange;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "job_name")
    @JsonIgnore
    private String jobName;

    @Column(name = "job_group")
    @JsonIgnore
    private String jobGroup;

    @Column(name = "trigger_name")
    @JsonIgnore
    private String triggerName;

    @Column(name = "trigger_group")
    @JsonIgnore
    private String triggerGroup;

}
