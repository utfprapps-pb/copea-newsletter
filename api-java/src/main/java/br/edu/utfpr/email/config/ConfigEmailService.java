package br.edu.utfpr.email.config;

import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ConfigEmailService {

    @Autowired
    ConfigEmailRepository configEmailRepository;

    public void saveConfigEmail(ConfigEmail configEmail) {
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

    public List<ConfigEmail> findAllConfigEmail() {
        return configEmailRepository.findAll();
    }

}
