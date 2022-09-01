package br.edu.utfpr.email.config.repository;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigEmailRepository extends JpaRepository<ConfigEmailEntity, Long> {
}
