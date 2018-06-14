package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountOfProductRepository extends CrudRepository<AmountOfProduct, Long> {
}
