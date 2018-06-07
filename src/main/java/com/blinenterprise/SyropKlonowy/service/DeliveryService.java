package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryBuilder;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.repository.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ProductWithQuantityService productWithQuantityService;

    @Autowired
    private WarehouseSectorService warehouseSectorService;

    private DeliveryBuilder deliveryTemplate = DeliveryBuilder.aDelivery();

    public void addProductToDelivery(Product product, int quantity) {
        deliveryTemplate.addProduct(product, quantity);
    }

    public void removeProductFromDelivery(String productName, int quantity) {
        deliveryTemplate.removeQuantityOfProduct(productName, quantity);
    }

    @Transactional
    public void createDeliveryFromCurrentTemplate() {
        deliveryTemplate.getListOfProducts().forEach(productWithQuantity ->
                productWithQuantityService.save(productWithQuantity)
        );
        Delivery delivery = deliveryTemplate.build();
        deliveryRepository.save(delivery);
        deliveryTemplate = new DeliveryBuilder();
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    public List<Delivery> findAllFrom(Date date) {
        return Lists.newArrayList(deliveryRepository.findAllByDeliveryDateAfter(date));
    }

    public void startHandlingADelivery(Long deliveryId) {
        Delivery delivery = findById(deliveryId).orElseThrow(IllegalArgumentException::new);
        delivery.beginDelivering();
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void placeProduct(Long deliveryId, Long productId, int amountPlaced, Long sectorId) {
        Delivery delivery = findById(deliveryId).orElseThrow(IllegalArgumentException::new);
        ProductWithQuantity productWithQuantityToPlace = delivery.getListOfProducts()
                .stream()
                .filter(productWithQuantity -> productWithQuantity.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        if (warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantityToPlace.getProduct(), amountPlaced, sectorId)) {
            delivery.notifyProductPlacement(productId, amountPlaced);
            deliveryRepository.save(delivery);
        }
    }

    public Delivery getCurrentTempate(){
        return deliveryTemplate.getTemplate();
    }

    public List<Delivery> findAllWithStatus(String deliveryStatus){
        return deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.valueOf(deliveryStatus.toUpperCase()));
    }
}
