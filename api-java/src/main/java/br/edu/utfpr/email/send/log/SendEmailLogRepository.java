package br.edu.utfpr.email.send.log;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendEmailLogRepository extends GenericRepository<SendEmailLog, Long> {
}
