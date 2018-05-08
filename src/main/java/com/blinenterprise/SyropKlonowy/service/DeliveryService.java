package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryBuilder;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
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
        Long destinationId = warehouseService.findByName(warehouseName).orElseThrow(IllegalArgumentException::new).getId();
        Delivery delivery = deliveryTemplate.build(destinationId);
        log.info("Performing delivery to warehouse " + warehouseName + " with id " + destinationId);
        delivery.getListOfProducts().forEach(productWithQuantity -> {
            warehouseService.addProductWithQuantity(productWithQuantity, warehouseName);
        });
        deliveryRepository.save(delivery);
        deliveryTemplate = new DeliveryBuilder();
    }

    public List<Delivery> findAllById(Long id) {
        return Lists.newArrayList(deliveryRepository.findAllById(id));
    }

    public List<Delivery> findAllForWarehouse(Long warehouseId) {
        return Lists.newArrayList(deliveryRepository.findAllByTargetWarehouseId(warehouseId));
    }

    public List<Delivery> findAllForWarehouse(String warehouseName) {
        return Lists.newArrayList(deliveryRepository.findAllByTargetWarehouseName(warehouseName));
    }

    public List<Delivery> findAllForWarehouseFrom(Long warehouseId, Date date) {
        return Lists.newArrayList(deliveryRepository.findAllByTargetWarehouseIdAndDeliveryDateAfter(warehouseId, date));
    }
}
