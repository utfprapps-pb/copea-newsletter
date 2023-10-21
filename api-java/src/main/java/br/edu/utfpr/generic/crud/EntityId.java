package br.edu.utfpr.generic.crud;

public interface EntityId<I> {
    void setId(I id);
    I getId();
}
