package br.edu.utfpr.email.email.repository;

import br.edu.utfpr.email.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}
