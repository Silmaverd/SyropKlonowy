package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery;
import com.blinenterprise.SyropKlonowy.repository.DeliveryRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<Delivery> findAllById(Long id) {
        return Lists.newArrayList(deliveryRepository.findAllById(id));
    }

    public List<Delivery> findAll() {
        return Lists.newArrayList(deliveryRepository.findAll());
    }

    public void createDelivery(Delivery delivery){deliveryRepository.save(delivery);}

}