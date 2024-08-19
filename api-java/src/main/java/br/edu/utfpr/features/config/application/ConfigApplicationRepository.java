package br.edu.utfpr.features.config.application;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigApplicationRepository extends GenericRepository<ConfigApplication, Long> {
}
