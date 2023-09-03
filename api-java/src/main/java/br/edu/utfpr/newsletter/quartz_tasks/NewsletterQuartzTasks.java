package br.edu.utfpr.newsletter.quartz_tasks;

import br.edu.utfpr.quartz.tasks.QuartzTasks;
import br.edu.utfpr.newsletter.Newsletter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "newsletter_qrtz_tasks")
@Entity
@Getter
@Setter
public class NewsletterQuartzTasks {

    @Id
    @SequenceGenerator(name = "newsletter_qrtz_tasks_id_sequence", sequenceName = "newsletter_qrtz_tasks_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "newsletter_qrtz_tasks_id_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_id")
    @NotNull
    private Newsletter newsletter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "qrtz_tasks_id")
    @NotNull
    private QuartzTasks quartzTask;

}
