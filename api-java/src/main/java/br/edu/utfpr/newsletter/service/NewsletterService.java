package br.edu.utfpr.newsletter.service;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import br.edu.utfpr.newsletter.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class NewsletterService {
    @Autowired
    NewsletterRepository newsletterRepository;

    public void save(NewsletterEntity newsletterEntity) {
        newsletterEntity.setDataInclusao(LocalDateTime.now());
        newsletterEntity.setNewsletter(new String(Base64.getDecoder().decode(newsletterEntity.getNewsletter())));
        newsletterRepository.save(newsletterEntity);
    }

    public void update(NewsletterEntity newsletterEntity) {
        newsletterEntity.setDataAlteracao(LocalDateTime.now());
        newsletterEntity.setNewsletter(new String(Base64.getDecoder().decode(newsletterEntity.getNewsletter())));
        newsletterRepository.save(newsletterEntity);
    }

    public String delete(Long id) {
        newsletterRepository.deleteById(id);
        return "Registro deletado com sucesso.";
    }

    public List<NewsletterEntity> findAll() {
        return newsletterRepository.findAll();
    }
}
