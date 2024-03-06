package br.edu.utfpr.generic.crud.resource.modelmapper;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.utils.ModelMapperUtils;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @param <T> Entity
 * @param <D> DTO
 * @param <I> ID type
 * @param <S> GenericService
 */
public abstract class GenericResourceDto<
        T extends EntityId<I>,
        D extends EntityId<I>,
        I,
        S extends GenericService> {

    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    @Getter
    private final ModelMapperUtils<T, D> modelMapperUtils;

    protected GenericResourceDto(Class<T> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.modelMapperUtils = new ModelMapperUtils<>(entityClass, dtoClass);
    }

    @Getter
    @Inject
    S service;

    @GET
    public List<D> get() {
        return modelMapperUtils.convertEntityListToDtoList(service.findAll());
    }

    @GET
    @Path("{id}")
    public D getById(@PathParam("id") I id) {
        return modelMapperUtils.convertToDto((T) service.findById(id));
    }

    @POST
    @Transactional
    public Response save(@Valid D dto) {
        T entity = getEntityFromDtoById(dto);
        return Response.status(Response.Status.CREATED).entity(service.save(entity)).build();
    }

    @PUT
    @Transactional
    public GenericResponse update(@Valid D dto) {
        T entity = getEntityFromDtoById(dto);
        return service.update(entity);
    }

    /**
     * Caso não possuir o id no DTO, somente é feito a conversão, caso existir,
     * buscar o registro do banco para depois realizar a conversão,
     * isso serve para evitar de setar nulo no banco para os campos
     * que existem somente na entidade e não no DTO.
     * @param dto
     * @return
     */
    public T getEntityFromDtoById(D dto) {
        if (Objects.isNull(dto.getId()))
            return modelMapperUtils.convertToEntity(dto);
        else {
            // TODO: quando tem propriedade bidirecional,
            //  fica nulo a entidade pai nos filhos e acaba ficando registro sem vínculo no banco,
            //  verificar como ajustar com o modelmapper,
            //  com o mapstruct consegui arrumar em cada mapper pra setar manualmente
            return modelMapperUtils.convertToEntity(dto, getDetachedEntityFromDb(dto.getId()));
        }
    }

    /**
     * Cria uma nova instância da entidade e passa todos os campos da
     * entidade encontrada no banco para a nova instância,
     * isso é feito para forçar o get de todos os campos na entidade do banco, inicializando as propriedades LAZY.
     * Tentei utilizar somente o detach do EntityManager na própria entidade do banco após converter,
     * para não precisar criar uma nova instância, porém assim,
     * se tiver propriedades LAZY que não foram carregadas antes de chamar o detach,
     * irá ocorrer o erro 'failed to lazily initialize a collection of role' ao chamar o get dessas propriedades.
     * @param id
     * @return
     */
    private T getDetachedEntityFromDb(I id) {
        T databaseEntity = (T) service.findById(id);
        T entityDetached = newEntityInstance();
        modelMapperUtils.getModelMapper().map(databaseEntity, entityDetached);
        service.getEntityManager().detach(databaseEntity);
        return entityDetached;
    }

    private T newEntityInstance() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar instância da classe " + entityClass.getName() + ".\nDetalhes: " + e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public GenericResponse deleteById(@PathParam("id") I id) {
        return service.deleteById(id);
    }

}
