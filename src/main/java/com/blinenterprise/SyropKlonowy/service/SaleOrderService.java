package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.config.ConfigContainer;
import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseSectorService warehouseSectorService;

    @Autowired
    private OrderClosureExecutor orderClosureExecutor;

    @Autowired
    private ConfigContainer configContainer;

    @Autowired
    private AmountOfProductService amountOfProductService;

    @Autowired
    private ClientService clientService;

    private Map<Long, SaleOrder> temporarySaleOrders = new HashMap<>();


    public void addProductToOrder(Long clientId, Long productId, Integer quantity) {
        if (clientService.findById(clientId) == null) {
            throw new IllegalArgumentException();
        }
        Product productToAdd = productService.findById(productId).orElseThrow(IllegalArgumentException::new);
        temporarySaleOrders.putIfAbsent(clientId, new SaleOrder(clientId, new Date(), new ArrayList<>(), BigDecimal.valueOf(0), SaleOrderStatus.NEW));
        temporarySaleOrders.get(clientId).addAmountOfProduct(new AmountOfProduct(productToAdd.getId(), quantity));
        temporarySaleOrders.get(clientId).recalculateTotalPrice(productService);
    }

    @Transactional
    public void confirmTempClientOrder(Long clientId) {
        if (!temporarySaleOrders.containsKey(clientId) || temporarySaleOrders.get(clientId).getAmountsOfProducts().isEmpty()) {
            throw new IllegalStateException();
        }
        temporarySaleOrders.get(clientId).getAmountsOfProducts().forEach(amountOfProduct ->
                amountOfProductService.save(amountOfProduct));

        Date closureDate = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(configContainer.getOrderClosureDelayInDays()));
        orderClosureExecutor.addClosureCommand(temporarySaleOrders.get(clientId).getId(), closureDate);

        SaleOrder saleOrderByClient = temporarySaleOrders.get(clientId);
        saleOrderRepository.save(saleOrderByClient);
        log.info("Successfully confirmed new order with id:" + saleOrderByClient.getId());

        saleOrderByClient.getAmountsOfProducts().forEach(amountOfProduct -> warehouseSectorService.reserveSaleOrderedAmountOfProduct(amountOfProduct));

        temporarySaleOrders.remove(clientId);
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

    public boolean closeById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.closeOrder()) {
            orderById.getAmountsOfProducts().forEach(amountOfProduct ->
                    warehouseSectorService.unReserveSaleOrderedAmountOfProduct(amountOfProduct));
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }

    public boolean payById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.payOrder()) {
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }

    public boolean sendById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.sendOrder()) {
            orderById.getAmountsOfProducts().forEach(amountOfProduct -> warehouseSectorService.removeSaleOrderedAmountOfProduct(amountOfProduct));
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }
}
