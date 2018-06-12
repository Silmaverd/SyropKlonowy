package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Iterable<Client> findAllByName(String name);

    @Query("SELECT DISTINCT c from Client as c JOIN SaleOrder as so ON c.id = so.clientId " +
            "WHERE so.dateOfOrder > ?1")
    public List<Client> findAllHavingOrdersSince(Date date);

}

