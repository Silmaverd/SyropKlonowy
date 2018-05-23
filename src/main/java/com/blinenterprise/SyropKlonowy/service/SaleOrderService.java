package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.config.ConfigContainer;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
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
import java.util.stream.Collectors;

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
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
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

        saleOrderByClient.getAmountsOfProducts().forEach(amountOfProduct -> warehouseSectorService.reserveAmountOfProduct(amountOfProduct));

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

    @Transactional
    public boolean closeById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.closeOrder()) {
            orderById.getAmountsOfProducts().forEach(amountOfProduct ->
                    warehouseSectorService.unReserveAmountOfProduct(amountOfProduct));
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean payById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.payOrder()) {
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean sendById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.sendOrder()) {
            orderById.getAmountsOfProducts().forEach(amountOfProduct -> warehouseSectorService.removeReservedAmountOfProduct(amountOfProduct));
            saleOrderRepository.save(orderById);
            return true;
        }
        return false;
    }

    public List<SaleOrder> findAllByClientId(Long clientId){
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        return saleOrderRepository.findAllByClientId(clientId);
    }


    public List<Product> findAllProductsOrderedByClient(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        ArrayList<Product> results = new ArrayList<>();
        saleOrderRepository.findAllByClientId(clientId).forEach(
                saleOrder ->
                        saleOrder.getAmountsOfProducts().forEach(
                                amountOfProduct ->
                                        results.add(productService.findById(amountOfProduct.getProductId()).get())));

        return results;
    }

    public BigDecimal findMaxPriceInClientOrders(Long clientId){
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        return saleOrderRepository.findMaxPriceInClientOrders(clientId);
    }

    public BigDecimal findMinPriceInClientOrders(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        return saleOrderRepository.findMinPriceInClientOrders(clientId);
    }

    public BigDecimal findMaxPriceOfProductInClientOrders(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        return saleOrderRepository.findMaxPriceOfProductInClientOrders(clientId);
    }

    public BigDecimal findAveragePriceOfProductInClientOrders(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        return saleOrderRepository.findAveragePriceOfProductInClientOrders(clientId);
    }

    public AmountOfProduct getAmountOfProduct(Object[] object){
        return new AmountOfProduct((Long)object[0], (int)(long) object[1]);
    }

    public List<AmountOfProduct> getListOfAmountOfProduct(List<Object[]> listOfObjects){
        return listOfObjects.stream().map(object -> new AmountOfProduct((Long) object[0], (int)(long) object[1])).collect(Collectors.toList());
    }


    public List<AmountOfProduct> findMostCommonlyPurchasedProducts(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        List<Object[]> listOfProductIdWithQuantity = saleOrderRepository.findProductIdFromAllOrdersWithSumOfQuantity(clientId);
        return getListOfAmountOfProduct(listOfProductIdWithQuantity);
    }

    public List<AmountOfProduct> findFrequentlyBoughtTogether(Long productId) {
        productService.findById(productId).orElseThrow(IllegalArgumentException::new);
        List<Object[]> listOfFrequentlyProduct = saleOrderRepository.findFrequentlyBoughtTogether(productId);
        return getListOfAmountOfProduct(listOfFrequentlyProduct);
    }


}
