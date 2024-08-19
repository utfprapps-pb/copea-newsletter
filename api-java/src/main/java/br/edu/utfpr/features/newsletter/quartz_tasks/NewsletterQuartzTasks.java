package br.edu.utfpr.features.newsletter.quartz_tasks;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.features.newsletter.Newsletter;
import br.edu.utfpr.features.quartz.tasks.QuartzTasks;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Table(name = "newsletter_qrtz_tasks")
@Entity
@Getter
@Setter
public class NewsletterQuartzTasks implements EntityId<Long> {

    public static final String JOB_DATA_NEWSLETTER_ID = "newsletterId";

    @Id
    @SequenceGenerator(name = "newsletter_qrtz_tasks_id_sequence", sequenceName = "newsletter_qrtz_tasks_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "newsletter_qrtz_tasks_id_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_id")
    @JsonBackReference
    @NotNull
    private Newsletter newsletter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "qrtz_tasks_id")
    @NotNull
    private QuartzTasks quartzTask;

}
