package br.edu.utfpr.generic.crud;

import br.edu.utfpr.newsletter.Newsletter;
import br.edu.utfpr.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {
}
