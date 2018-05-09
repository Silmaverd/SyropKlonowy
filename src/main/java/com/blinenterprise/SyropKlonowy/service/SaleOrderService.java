package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductWithQuantityService productWithQuantityService;

    @Autowired
    private OrderClosureExecutor orderClosureExecutor;

    private Map<Long, SaleOrder> temporarySaleOrders = new HashMap<>();


    public void addProductToOrder(Long clientId, Long productId, Integer quantity) {
        if (!temporarySaleOrders.containsKey(clientId)) {
            throw new IllegalStateException();
        }
        if (!productService.findById(productId).isPresent()) {
            throw new IllegalArgumentException();
        }

        temporarySaleOrders.putIfAbsent(clientId, new SaleOrder(clientId, new Date(), new ArrayList<ProductWithQuantity>(), BigDecimal.valueOf(0), SaleOrderStatus.NEW));

        temporarySaleOrders.get(clientId).addProductWithQuantity(new ProductWithQuantity(productService.findById(productId).get(), quantity));
        temporarySaleOrders.get(clientId).recalculateTotalPrice();
    }

    @Transactional
    public void confirmTempClientOrder(Long clientId) {
        if (!temporarySaleOrders.containsKey(clientId) || temporarySaleOrders.get(clientId).getProductsWithQuantities().isEmpty()) {
            throw new IllegalStateException();
        }
        temporarySaleOrders.get(clientId).getProductsWithQuantities().forEach(productWithQuantity -> {
            productWithQuantityService.save(productWithQuantity);
        });
        Date now = new Date();
        orderClosureExecutor.addClosureCommand(temporarySaleOrders.get(clientId).getId(), new Date(now.getTime() + (1000 * 60 * 60 * 24 * 30)));
        saleOrderRepository.save(temporarySaleOrders.get(clientId));
        log.info("Successfully confirmed new order with id:" + temporarySaleOrders.get(clientId).getId());
        temporarySaleOrders.put(clientId, null);
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

    public void payById(Long id) {
        Optional<SaleOrder> orderById = saleOrderRepository.findById(id);
        if (orderById.isPresent()) {
            orderById.get().payOrder();
            saleOrderRepository.save(orderById.get());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void sendById(Long id) {
        Optional<SaleOrder> orderById = saleOrderRepository.findById(id);
        if (orderById.isPresent()) {
            orderById.get().sendOrder();
            saleOrderRepository.save(orderById.get());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
