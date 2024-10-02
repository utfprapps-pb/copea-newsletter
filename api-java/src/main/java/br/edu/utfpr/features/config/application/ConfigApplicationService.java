package br.edu.utfpr.features.config.application;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericErrorResponse;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class ConfigApplicationService extends GenericService<ConfigApplication, Long, ConfigApplicationRepository> {

    public ConfigApplication getOneOrElseThrowException() {
        List<ConfigApplication> configApplicationList = getRepository().findAll();
        if (configApplicationList.isEmpty())
            throw new ValidationException(
                    "As configurações da aplicação não existe. Para continuar, " +
                            "é necessário realizar o cadatro por meio da tela de configuração."
            );

        return configApplicationList.get(0);
    }

    @Override
    public GenericErrorResponse save(ConfigApplication configApplication) {
        validJustOneConfig(configApplication);
        return super.save(configApplication);
    }

    @Override
    public GenericErrorResponse update(ConfigApplication configApplication) {
        validJustOneConfig(configApplication);
        return super.update(configApplication);
    }

    private void validJustOneConfig(ConfigApplication configApplication) {
        List<ConfigApplication> configApplicationList = getRepository().findAll();
        if (configApplicationList.isEmpty())
            return;

        ConfigApplication configApplicationDatabase = configApplicationList.get(0);
        if (Objects.equals(configApplicationDatabase.getId(), configApplication.getId()))
            return;

        throw new ValidationException(
                "Já existe um cadastro com o id " + configApplicationDatabase.getId() +
                        "para as configurações da aplicação. Para continuar, atualize a existente.");
    }

}
