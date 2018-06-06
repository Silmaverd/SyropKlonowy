package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    Optional<Delivery> findById(Long id);

    List<Delivery> findAllByDeliveryDateAfter(Date date);

    List<Delivery> findAllByDeliveryStatusOrDeliveryStatus(DeliveryStatus deliveryStatus1, DeliveryStatus deliveryStatus2);
}
