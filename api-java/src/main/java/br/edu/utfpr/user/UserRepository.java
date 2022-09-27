package br.edu.utfpr.user;

import br.edu.utfpr.generic.crud.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    List<User> findByUsername(String username);

}
