package com.blinenterprise.SyropKlonowy.service;


import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public abstract class CrudService<T, ID, Repository extends CrudRepository<T, ID>> {

    @Autowired
    protected Repository repository;

    public boolean exists(ID id){
        return repository.existsById(id);
    }

    public long count(){
        return repository.count();
    }

    public List<T> findAll(){
        return Lists.newArrayList(repository.findAll());
    }

    public T findById(ID id) {
        Optional<T> productById = repository.findById(id);
        return productById.orElse(null);
    }

    public void save(T type) {
        repository.save(type);
    }

    public void delete(T type) {
        repository.delete(type);
    }




}
