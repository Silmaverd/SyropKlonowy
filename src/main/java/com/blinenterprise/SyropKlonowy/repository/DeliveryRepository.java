package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    Optional<Delivery> findById(Long id);

    List<Delivery> findAllByDeliveryDateAfter(Date date);
}
