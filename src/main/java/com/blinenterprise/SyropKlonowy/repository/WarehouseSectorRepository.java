package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseSectorRepository extends CrudRepository<WarehouseSector, Long> {

    Optional<WarehouseSector> findByName(String name);

    @Query("select ws from WarehouseSector ws join ws.amountOfProducts aop where aop.productId=:productId order by aop.quantity desc")
    Iterable<WarehouseSector> findAllContainingProductOrderedASCByProductId(@Param("productId") Long productId);

    @Query("select ws from WarehouseSector ws join ws.saleOrderedAmountOfProducts soaop where soaop.productId=:productId order by soaop.quantity asc")
    Iterable<WarehouseSector> findAllContainingSaleOrderedProductOrderedASCByProductId(@Param("productId") Long productId);
}
