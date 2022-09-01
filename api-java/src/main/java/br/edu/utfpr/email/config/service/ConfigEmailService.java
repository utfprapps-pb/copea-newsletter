package br.edu.utfpr.email.config.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.repository.ConfigEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ConfigEmailService {

    @Autowired
    ConfigEmailRepository configEmailRepository;

    public void saveConfigEmail(ConfigEmailEntity configEmail) {
        configEmailRepository.save(configEmail);
    }

    public String deleteConfigEmail(Long id) {
        if (configEmailRepository.existsById(id)) {
            configEmailRepository.deleteById(id);
            return "Registro deletado com sucesso.";
        } else {
            return "Registro não econtrado.";
        }
    }

    public List<ConfigEmailEntity> findAllConfigEmail() {
        return configEmailRepository.findAll();
    }

}
