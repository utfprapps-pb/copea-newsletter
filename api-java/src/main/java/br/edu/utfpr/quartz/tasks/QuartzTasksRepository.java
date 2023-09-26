package br.edu.utfpr.quartz.tasks;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuartzTasksRepository extends GenericRepository<QuartzTasks, Long> {

    Optional<QuartzTasks> findByJobNameAndJobGroupAndTriggerNameAndTriggerGroup(
            String jobName, String jobGroup, String triggerName, String triggerGroup
    );

}
