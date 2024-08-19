package br.edu.utfpr.auth.context;

import br.edu.utfpr.generic.crud.GenericRepository;
import lombok.Getter;
import lombok.Setter;

import jakarta.inject.Inject;
import java.util.Objects;

public abstract class AuthContextGeneric<T, ID, R extends GenericRepository<T, ID>> {

    @Inject
    @Getter
    R repository;

    @Setter
    @Getter
    private ID username;

    public T findByToken() {
        if (Objects.isNull(username))
            return null;
        else
            return repository.findById(username).get();
    }

}
