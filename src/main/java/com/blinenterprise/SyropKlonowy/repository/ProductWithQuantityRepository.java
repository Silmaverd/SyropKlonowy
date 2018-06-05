package com.blinenterprise.SyropKlonowy.repository;


import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface ProductWithQuantityRepository extends CrudRepository<ProductWithQuantity, Long> {
}
