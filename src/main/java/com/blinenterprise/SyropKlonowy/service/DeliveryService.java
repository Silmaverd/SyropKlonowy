package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryBuilder;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.DeliveryRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ProductWithQuantityService productWithQuantityService;

    @Autowired
    private WarehouseService warehouseService;

    private DeliveryBuilder deliveryTemplate = new DeliveryBuilder();

    public void addProductToDelivery(Product product, int quantity){
        deliveryTemplate.addProduct(product, quantity);
    }

    @Transactional
    public void performDeliveryFromCurrentTemplate(String warehouseName){
        deliveryTemplate.getListOfProducts().forEach(productWithQuantity -> {
            productWithQuantityService.save(productWithQuantity);
        });
        Delivery delivery = deliveryTemplate.build();
        delivery.getListOfProducts().forEach(productWithQuantity -> {

        });
        deliveryRepository.save(delivery);
        deliveryTemplate = new DeliveryBuilder();
    }

    public List<Delivery> findAllById(Long id) {
        return Lists.newArrayList(deliveryRepository.findAllById(id));
    }

    public List<Delivery> findAll() {
        return Lists.newArrayList(deliveryRepository.findAll());
    }
}
