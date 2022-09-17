package br.edu.utfpr.newsletter.service;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import br.edu.utfpr.newsletter.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.enterprise.context.ApplicationScoped;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NewsletterService {
    @Autowired
    NewsletterRepository newsletterRepository;

    public void save(NewsletterEntity newsletterEntity) {
        newsletterEntity.setInclusionDate(LocalDateTime.now());
        newsletterRepository.save(newsletterEntity);
    }

    public void update(NewsletterEntity newsletterEntity) {
        newsletterEntity.setAlterationDate(LocalDateTime.now());
        newsletterRepository.save(newsletterEntity);
    }

    public String delete(Long id) {
        if (newsletterRepository.existsById(id)) {
            newsletterRepository.deleteById(id);
            return "Registro deletado com sucesso.";
        } else {
            return "Registro não econtrado.";
        }
    }

    public List<NewsletterEntity> findAll() {
        return newsletterRepository.findAll();
    }

    public NewsletterEntity findNewsletterById(Long id) throws Exception {
        Optional<NewsletterEntity> newsletterEntity = newsletterRepository.findById(id);
        if (newsletterEntity.isPresent())
            return newsletterEntity.get();
        else
            throw new Exception("Newsletter não econtrada.");
    }
}
