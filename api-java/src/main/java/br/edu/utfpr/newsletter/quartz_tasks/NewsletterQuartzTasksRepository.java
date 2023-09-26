package br.edu.utfpr.newsletter.quartz_tasks;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterQuartzTasksRepository extends GenericRepository<NewsletterQuartzTasks, Long> {

}