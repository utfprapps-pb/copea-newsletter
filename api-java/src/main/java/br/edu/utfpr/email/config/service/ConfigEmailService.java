package br.edu.utfpr.email.config.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ConfigEmailService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void saveConfigEmail(ConfigEmailEntity configEmail) {
        entityManager.persist(configEmail);
    }

    @Transactional
    public void updateConfigEmail(ConfigEmailEntity configEmail) {
        entityManager.merge(configEmail);
    }

    @Transactional
    public String deleteConfigEmail(Long id) {
        ConfigEmailEntity configEmail = entityManager.find(ConfigEmailEntity.class, id);
        if (configEmail == null)
            return "Registro não encontrado.";
        else
            entityManager.remove(configEmail);
        return "Registro deletado com sucesso.";
    }

    public List<ConfigEmailEntity> findAllConfigEmail() {
        return entityManager.createNamedQuery(ConfigEmailEntity.class.getSimpleName()+".findAll", ConfigEmailEntity.class).getResultList();
    }

}
