package br.edu.utfpr.newsletter.repository;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<NewsletterEntity, Long> {

    Optional<NewsletterEntity> findById(Long id);

}
