package br.edu.utfpr.generic.crud;

import br.edu.utfpr.auth.AuthSecurityFilter;
import br.edu.utfpr.reponses.GenericErrorResponse;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public abstract class GenericService<T, ID, R extends GenericRepository> {

    @Inject
    AuthSecurityFilter authSecurityFilter;

    @Inject
    R repository;

    @Inject
    EntityManager entityManager;

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findById(ID id) {
        Optional<T> optional = repository.findById(id);
        if (!optional.isPresent())
            throwNotFoundException();

        return optional.get();
    }

    public GenericErrorResponse save(T entity) {
        setDefaultValuesWhenNew(entity);
        repository.save(entity);
        return GenericErrorResponse.getGenericResponse("Registro(s) incluído(s) com sucesso.", Response.Status.CREATED.getStatusCode());
    }

    public void setDefaultValuesWhenNew(T entity) {
    }

    public GenericErrorResponse update(T entity) {
        repository.save(entity);
        return GenericErrorResponse.getGenericResponse("Registro(s) atualizado(s) com sucesso.", Response.Status.OK.getStatusCode());
    }

    public GenericErrorResponse deleteById(ID id) {
        if (!repository.existsById(id))
            throwNotFoundException();

        repository.deleteById(id);

        return GenericErrorResponse.getGenericResponse("Registro deletado com sucesso.", Response.Status.OK.getStatusCode());
    }

    public void throwNotFoundException() {
        throw new NotFoundException("Registro não encontrado.");
    }

}
