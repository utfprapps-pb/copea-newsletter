package br.edu.utfpr.email.config;

import br.edu.utfpr.generic.crud.GenericRepository;
import br.edu.utfpr.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigEmailRepository extends GenericRepository<ConfigEmail, Long> {

    Optional<ConfigEmail> findByIdAndUser(Long id, User user);

    List<ConfigEmail> findByUser(User user);

}
