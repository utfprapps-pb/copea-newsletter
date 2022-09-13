package br.edu.utfpr.email.email.repository;

import br.edu.utfpr.email.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    List<EmailEntity> findAllByEmailContaining(String email);

}
