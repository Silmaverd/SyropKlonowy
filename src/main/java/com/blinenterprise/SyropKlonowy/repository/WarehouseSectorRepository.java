package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseSectorRepository extends CrudRepository<WarehouseSector, Long> {

    Optional<WarehouseSector> findByName(String name);
}
