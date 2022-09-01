package br.edu.utfpr.email.config.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.repository.ConfigEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ConfigEmailService {

    @Autowired
    ConfigEmailRepository configEmailRepository;

    public void saveConfigEmail(ConfigEmailEntity configEmail) {
        configEmailRepository.save(configEmail);
    }

    public String deleteConfigEmail(Long id) {
        configEmailRepository.deleteById(id);
        return "Registro deletado com sucesso.";
    }

    public List<ConfigEmailEntity> findAllConfigEmail() {
        return configEmailRepository.findAll();
    }

}
