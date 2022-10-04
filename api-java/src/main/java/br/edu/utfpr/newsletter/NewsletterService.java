package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;

import javax.enterprise.context.RequestScoped;
import java.time.LocalDateTime;
import java.util.Objects;

@RequestScoped
public class NewsletterService extends GenericService<Newsletter, Long, NewsletterRepository> {

    @Override
    public GenericResponse save(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public GenericResponse update(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    private GenericResponse saveOrUpdate(Newsletter entity) {
        setDatesByNewOrUpdate(entity);
        return super.save(entity);
    }

    private void setDatesByNewOrUpdate(Newsletter entity) {
        if (!Objects.isNull(entity.getId()) && getRepository().existsById(entity.getId()))
            entity.setDataAlteracao(LocalDateTime.now());
        else
            entity.setDataInclusao(LocalDateTime.now());
    }

}
