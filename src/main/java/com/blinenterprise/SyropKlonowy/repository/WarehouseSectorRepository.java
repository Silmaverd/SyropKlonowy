package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface WarehouseSectorRepository extends CrudRepository<WarehouseSector, Long> {

    Optional<WarehouseSector> findByName(String name);

    @Query("select ws from WarehouseSector ws join ws.notReservedAmountOfProducts nraop on nraop.productId=:productId order by nraop.quantity asc")
    List<WarehouseSector> findAllContainingNotReservedProductOrderedASCByProductId(@Param("productId") Long productId);

    @Query("select ws from WarehouseSector ws join ws.reservedAmountOfProducts raop on raop.productId=:productId order by raop.quantity asc")
    List<WarehouseSector> findAllContainingReservedProductOrderedASCByProductId(@Param("productId") Long productId);

    @Query("select ws from WarehouseSector ws join ws.reservedAmountOfProducts raop on raop.productId=:productId order by raop.quantity desc")
    List<WarehouseSector> findAllContainingReservedProductOrderedDESCByProductId(@Param("productId") Long productId);
}
