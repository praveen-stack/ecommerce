package com.ecommerce.usermanagementservice.components;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ObjectMapper<D, E> {

    @Autowired
    private ModelMapper modelMapper;

    public abstract Class<D> getDtoClass();

    public abstract Class<E> getEntityClass();

    public D toDto(E entity) {
        return (D)this.modelMapper.map(entity, this.getDtoClass());
    }
    public E toEntity(D dto) {
        return (E)this.modelMapper.map(dto, this.getEntityClass());
    }
}
