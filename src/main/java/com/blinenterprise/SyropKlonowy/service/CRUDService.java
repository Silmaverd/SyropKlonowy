package com.blinenterprise.SyropKlonowy.service;

import java.util.List;

public interface CRUDService<T, I> {

    T create(T t);

    T findById(I id);

    List<T> findAll();

    T update(T t);

    void delete(I id);
}
