package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Iterable<Client> findAllByName(String name);
}

