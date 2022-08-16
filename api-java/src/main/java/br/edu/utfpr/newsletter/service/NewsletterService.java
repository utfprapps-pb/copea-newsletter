package br.edu.utfpr.newsletter.service;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class NewsletterService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public void save(NewsletterEntity newsletterEntity) {
        newsletterEntity.setDataInclusao(LocalDateTime.now());
        newsletterEntity.setNewsletter(new String(Base64.getDecoder().decode(newsletterEntity.getNewsletter())));
        entityManager.persist(newsletterEntity);
    }

    @Transactional
    public void update(NewsletterEntity newsletterEntity) {
        newsletterEntity.setDataAlteracao(LocalDateTime.now());
        newsletterEntity.setNewsletter(new String(Base64.getDecoder().decode(newsletterEntity.getNewsletter())));
        entityManager.merge(newsletterEntity);
    }

    @Transactional
    public String delete(Long id) {
        NewsletterEntity newsletterEntity = entityManager.find(NewsletterEntity.class, id);
        if (newsletterEntity == null)
            return "Registro não encontrado.";
        else
            entityManager.remove(newsletterEntity);
        return "Registro deletado com sucesso.";
    }

    public List<NewsletterEntity> findAll() {
        return entityManager.createNamedQuery(NewsletterEntity.class.getSimpleName()+".findAll", NewsletterEntity.class).getResultList();
    }
}
