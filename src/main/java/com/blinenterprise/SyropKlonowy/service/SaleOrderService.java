package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderedProduct;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SaleOrderedProductService saleOrderedProductService;

    @Autowired
    private DeliveryService deliveryService;

    private SaleOrder currentSaleOrder = null;

    public void startOrder(Long clientId, Date dateOfOrder) {
        if (clientService.findById(clientId) == null) {
            throw new IllegalArgumentException();
        }
        currentSaleOrder = new SaleOrder(clientId, dateOfOrder, new LinkedList<SaleOrderedProduct>(), new BigDecimal(0), SaleOrderStatus.NEW);
    }

    public void addProductToCurrentOrder(Long productId, Integer quantity) {
        if (currentSaleOrder == null) {
            throw new IllegalStateException();
        }
        if (!productService.findById(productId).isPresent()) {
            throw new IllegalArgumentException();
        }
        BigDecimal saleOrderedProductPrice = productService.findById(productId).get().getPrice().multiply(BigDecimal.valueOf(quantity));
        currentSaleOrder.addSaleOrderedProduct(new SaleOrderedProduct(productId, quantity));
        currentSaleOrder.setTotalPrice(currentSaleOrder.getTotalPrice().add(saleOrderedProductPrice));
    }

    @Transactional
    public void confirmCurrentOrder() {
        if (currentSaleOrder == null || currentSaleOrder.getSaleOrderedProducts().isEmpty()) {
            throw new IllegalStateException();
        }
        currentSaleOrder.getSaleOrderedProducts().forEach(saleOrderedProduct -> {
            saleOrderedProductService.save(saleOrderedProduct);
        });
        saleOrderRepository.save(currentSaleOrder);
        log.info("Successfully confirmed new order with id:" + currentSaleOrder.getId());
        currentSaleOrder = null;
    }

    public void sendOrderByIdToWarehouse(Long orderId, String warehouseName) {
        SaleOrder tempOrder = saleOrderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        tempOrder.sendOrder();
        tempOrder.getSaleOrderedProducts().forEach(saleOrderedProduct ->
                deliveryService.addProductToDelivery(
                        productService.findById(saleOrderedProduct.getProductId()).get(),
                        saleOrderedProduct.getQuantity()));
        deliveryService.performDeliveryFromCurrentTemplate(warehouseName);
        saleOrderRepository.save(tempOrder);
        log.info("Sale order with id:" + tempOrder.getId() + " has been successfully sent out to warehouse " + warehouseName);
    }

    public SaleOrder create(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    public SaleOrder findById(Long id) {
        Optional<SaleOrder> warehouseById = saleOrderRepository.findById(id);
        return warehouseById.orElse(null);
    }

    public List<SaleOrder> findAll() {
        return Lists.newArrayList(saleOrderRepository.findAll());
    }

    public void deleteById(Long id) {
        saleOrderRepository.deleteById(id);
    }

    public void closeById(Long id) {
        Optional<SaleOrder> orderById = saleOrderRepository.findById(id);
        if (orderById.isPresent()) {
            orderById.get().closeOrder();
            saleOrderRepository.save(orderById.get());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
