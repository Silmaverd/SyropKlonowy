package com.blinenterprise.SyropKlonowy.repository;


import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWithQuantityRepository extends CrudRepository<ProductWithQuantity, Long> {
}
