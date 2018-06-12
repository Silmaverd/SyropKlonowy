package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Iterable<Client> findAllByName(String name);
}

