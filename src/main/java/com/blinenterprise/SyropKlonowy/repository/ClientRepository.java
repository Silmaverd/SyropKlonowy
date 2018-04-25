package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Client findById(long id);

}

