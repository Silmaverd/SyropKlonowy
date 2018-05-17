package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseSectorRepository extends CrudRepository<WarehouseSector, Long> {

    Optional<WarehouseSector> findByName(String name);

    @Query("select ws from WarehouseSector ws join ws.amountOfProducts aop on aop.productId=:productId order by aop.quantity asc")
    List<WarehouseSector> findAllContainingProductOrderedASCByProductId(@Param("productId") Long productId);

    @Query("select ws from WarehouseSector ws join ws.saleOrderedAmountOfProducts soaop on soaop.productId=:productId order by soaop.quantity asc")
    List<WarehouseSector> findAllContainingSaleOrderedProductOrderedASCByProductId(@Param("productId") Long productId);

    @Query("select ws from WarehouseSector ws join ws.saleOrderedAmountOfProducts soaop on soaop.productId=:productId order by soaop.quantity desc")
    List<WarehouseSector> findAllContainingSaleOrderedProductOrderedDESCByProductId(@Param("productId") Long productId);
}
