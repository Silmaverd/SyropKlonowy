package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    List<Delivery> findAllById(Long id);

    List<Delivery> findAllByTargetWarehouseId(Long id);

    @Query("SELECT delivery" +
           " FROM Delivery delivery " +
           " INNER JOIN Warehouse warehouse ON (delivery.targetWarehouseId = warehouse.id)" +
           " WHERE 1=1" +
           " AND warehouse.name = ?1")
    List<Delivery> findAllByTargetWarehouseName(String name);

    List<Delivery> findAllByTargetWarehouseIdAndDeliveryDateAfter(Long warehouseId, Date date);
}
