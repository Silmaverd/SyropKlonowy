package com.blinenterprise.SyropKlonowy.domain.Delivery.processing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrentlyProccessedDeliveriesRepo extends CrudRepository<DeliveryProcessed, Long>{

    public Optional<DeliveryProcessed> findByDeliveryId(Long id);
}
