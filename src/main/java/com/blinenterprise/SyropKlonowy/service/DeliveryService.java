package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryBuilder;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Delivery.processing.CurrentlyProccessedDeliveriesRepo;
import com.blinenterprise.SyropKlonowy.domain.Delivery.processing.DeliveryProcessed;
import com.blinenterprise.SyropKlonowy.domain.Product;
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
    private CurrentlyProccessedDeliveriesRepo currentlyProccessedDeliveriesRepo;

    @Autowired
    private ProductWithQuantityService productWithQuantityService;

    @Autowired
    private WarehouseService warehouseService;

    private DeliveryBuilder deliveryTemplate = DeliveryBuilder.aDelivery();

    public void addProductToDelivery(Product product, int quantity) {
        deliveryTemplate.addProduct(product, quantity);
    }

    @Transactional
    public void createDeliveryFromCurrentTemplate() {
        deliveryTemplate.getListOfProducts().forEach(productWithQuantity -> {
            productWithQuantityService.save(productWithQuantity);
        });
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

    public Optional<DeliveryProcessed> findDeliveryInProgressForId(Long id){
        return currentlyProccessedDeliveriesRepo.findByDeliveryId(id);
    }

    public void startHandlingADelivery(Long deliveryId) {
        Delivery delivery = findById(deliveryId).get();
        currentlyProccessedDeliveriesRepo.save(new DeliveryProcessed(getProductsCopy(delivery.getListOfProducts()), deliveryId));
        delivery.deliveryStatus = DeliveryStatus.IN_PROCESS;
        deliveryRepository.save(delivery);
    }

    public void placeProduct(Long deliveryId, Long productId, int amountPlaced, Long sectorId) {
        DeliveryProcessed deliveryProcessed = currentlyProccessedDeliveriesRepo.findByDeliveryId(deliveryId).get();
        Delivery delivery = findById(deliveryId).get();
        /* TODO: PLACE PRODUCT AT RIGHT SECTOR */
        deliveryProcessed.decreaseQuantityOfProduct(productId, amountPlaced);
        if (deliveryProcessed.isDeliveryFinished()) {
            currentlyProccessedDeliveriesRepo.delete(deliveryProcessed);
            delivery.deliveryStatus = DeliveryStatus.DONE;
            deliveryRepository.save(delivery);
            log.info("Delivery completed!");
        }
    }

    private List<ProductWithQuantity> getProductsCopy(List<ProductWithQuantity> productWithQuantities){
        ArrayList<ProductWithQuantity> copy = new ArrayList<>();
        Collections.copy(productWithQuantities, copy);
        System.out.println(productWithQuantities);
        System.out.println(copy);
        return copy;
    }
}
