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
        convertFieldNewsletterForBase64(newsletterEntity);
        newsletterRepository.save(newsletterEntity);
    }

    public void update(NewsletterEntity newsletterEntity) {
        newsletterEntity.setDataAlteracao(LocalDateTime.now());
        convertFieldNewsletterForBase64(newsletterEntity);
        newsletterRepository.save(newsletterEntity);
    }

    private void convertFieldNewsletterForBase64(NewsletterEntity newsletterEntity) {
        newsletterEntity.setNewsletter(new String(Base64.getDecoder().decode(newsletterEntity.getNewsletter())));
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
}
