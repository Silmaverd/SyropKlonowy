package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.converter.AmountOfProductConverter;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private AmountOfProductService amountOfProductService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private Environment environment;


    private Map<Long, SaleOrder> temporarySaleOrders = new HashMap<>();

    public void addProductToOrder(Long clientId, Long productId, Integer quantity) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        Integer productAvailableQuantity = 0;
        Product productToAdd = productService.findById(productId).orElseThrow(IllegalArgumentException::new);
        temporarySaleOrders.putIfAbsent(clientId, new SaleOrder(clientId, new Date(), new ArrayList<>(), BigDecimal.valueOf(0), SaleOrderStatus.NEW));

        productAvailableQuantity += warehouseSectorService.findQuantityOfNotReservedProductOnAllSectorsByProductId(productId);
        if (temporarySaleOrders.get(clientId).getAmountOfProductWithProductId(productId) != null) {
            productAvailableQuantity -= temporarySaleOrders.get(clientId).getAmountOfProductWithProductId(productId).getQuantity();
        }
        if (productAvailableQuantity < quantity) {
            throw new IllegalArgumentException("Not enough unreserved products in the warehouse.");
        }

        temporarySaleOrders.get(clientId).addAmountOfProduct(new AmountOfProduct(productToAdd.getId(), quantity));
        temporarySaleOrders.get(clientId).recalculateTotalPrice(productService);
    }

    public void removeProductFromOrder(Long clientId, Long productId, Integer quantity) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        SaleOrder selectedOrder = temporarySaleOrders.get(clientId);
        if (selectedOrder == null) throw new IllegalArgumentException("That order does not exist");
        selectedOrder.removeQuantityOfProductFromProductsToOrder(productId, quantity);
        selectedOrder.recalculateTotalPrice(productService);
    }

    @Transactional
    public void confirmTempClientOrder(Long clientId) {
        if (!temporarySaleOrders.containsKey(clientId) || temporarySaleOrders.get(clientId).getProductsToOrder().isEmpty()) {
            throw new IllegalStateException();
        }
        temporarySaleOrders.get(clientId).getProductsToOrder().forEach(amountOfProduct ->
                amountOfProductService.save(amountOfProduct));

        Date closureDate = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(Integer.parseInt(environment.getProperty("orderClosureDelayInDays"))));
        orderClosureExecutor.addClosureCommand(temporarySaleOrders.get(clientId).getId(), closureDate);

        SaleOrder saleOrderByClient = temporarySaleOrders.get(clientId);
        saleOrderRepository.save(saleOrderByClient);
        log.info("Successfully confirmed new order with id:" + saleOrderByClient.getId());

        saleOrderByClient.getProductsToOrder().forEach(amountOfProduct -> warehouseSectorService.reserveAmountOfProduct(amountOfProduct));

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

    public List<SaleOrder> findAllBySaleOrderStatus(SaleOrderStatus saleOrderStatus){
        return Lists.newArrayList(saleOrderRepository.findAllBySaleOrderStatus(saleOrderStatus));
    }

    @Transactional
    public boolean closeById(Long id) {
        SaleOrder orderById = saleOrderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (orderById.closeOrder()) {
            orderById.getProductsToOrder().forEach(amountOfProduct ->
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
            orderById.getProductsToOrder().forEach(amountOfProduct -> warehouseSectorService.removeReservedAmountOfProduct(amountOfProduct));
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
                        saleOrder.getProductsToOrder().forEach(
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

    public List<AmountOfProduct> findMostCommonlyPurchasedProducts(Long clientId) {
        clientService.findById(clientId).orElseThrow(IllegalArgumentException::new);
        if (saleOrderRepository.findAllByClientId(clientId).isEmpty()) {
            throw new IllegalArgumentException("Client has no orders.");
        }
        List<Object[]> listOfProductIdWithQuantity = saleOrderRepository.findProductIdFromAllOrdersWithSumOfQuantity(clientId);
        return AmountOfProductConverter.getAmountOfProductListFromLongAndLong(listOfProductIdWithQuantity);
    }

    public List<AmountOfProduct> findFrequentlyBoughtTogether(Long productId) {
        productService.findById(productId).orElseThrow(IllegalArgumentException::new);
        List<Object[]> listOfFrequentlyProduct = saleOrderRepository.findFrequentlyBoughtTogether(productId);
        return AmountOfProductConverter.getAmountOfProductListFromLongAndLong(listOfFrequentlyProduct);
    }

    public List<AmountOfProduct> findFrequentlyBoughtInLastWeek(){
        List<Object[]> listOfFrequentlyProduct = saleOrderRepository.findFrequentlyBoughtInLastWeek();
        return AmountOfProductConverter.getAmountOfProductListFromBigIntegerAndBigInteger(listOfFrequentlyProduct);
    }

    public List<AmountOfProduct> findFrequentlyBoughtInLastWeek(Enterprise enterpriseType){
        List<Object[]> listOfFrequentlyProduct = saleOrderRepository.findFrequentlyBoughtInLastWeek(enterpriseType.name(),
                Integer.parseInt(environment.getProperty("productAmountToLoad")));
        return AmountOfProductConverter.getAmountOfProductListFromBigIntegerAndBigInteger(listOfFrequentlyProduct);
    }

    public List<AmountOfProduct> findBoughtProductsSum(){
        List<Object[]> listOfBoughtProductsSum = saleOrderRepository.findBoughtProductsSum();
        return AmountOfProductConverter.getAmountOfProductListFromBigIntegerAndBigDecimal(listOfBoughtProductsSum);
    }

    public BigDecimal findIncomeFromOrders(Date startDate, Date endDate){
        return saleOrderRepository.findIncomeFromOrders(startDate, endDate);
    }

    public Optional<SaleOrder> findTemporaryOrderOfClient(Long clientId) {
        return Optional.of(temporarySaleOrders.get(clientId));
    }

    public List<SaleOrder> findAllSaleOrdersSince(Date date){
        return saleOrderRepository.findAllByDateOfOrderAfter(date);
    }

    public List<SaleOrder> findAllSaleOrdersBetweenDates(Date fromDate, Date toDate) {
        return saleOrderRepository.findAllByDateOfOrderBetween(fromDate, toDate);
    }

    public Map<Enterprise, Integer> findEnterpriseClientsWithOrderVolumeBetweenDates(Date fromDate, Date toDate) {
        Map<Enterprise, Integer> enterpriseVolumeMap = new HashMap<>();
        for (Enterprise enterpriseValue : Enterprise.values()) {
            enterpriseVolumeMap.putIfAbsent(enterpriseValue, 0);
        }
        saleOrderRepository.findAllByDateOfOrderBetween(fromDate, toDate).forEach(saleOrder -> {
            if (saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.PAID) || saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.SENT)) {
                clientService.findById(saleOrder.getClientId()).ifPresent(client ->
                        enterpriseVolumeMap.put(client.getEnterpriseType(),
                                enterpriseVolumeMap.get(client.getEnterpriseType()) + saleOrder.getTotalVolumeOfProducts()));
            }
        });
        return enterpriseVolumeMap;
    }

    public Map<Enterprise, BigDecimal> findEnterpriseClientsWithOrderValueBetweenDates(Date fromDate, Date toDate) {
        Map<Enterprise, BigDecimal> enterpriseValueMap = new HashMap<>();
        for (Enterprise enterpriseValue : Enterprise.values()) {
            enterpriseValueMap.putIfAbsent(enterpriseValue, new BigDecimal(0));
        }
        saleOrderRepository.findAllByDateOfOrderBetween(fromDate, toDate).forEach(saleOrder -> {
            if (saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.PAID) || saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.SENT)) {
                clientService.findById(saleOrder.getClientId()).ifPresent(client ->
                    enterpriseValueMap.put(client.getEnterpriseType(),
                            enterpriseValueMap.get(client.getEnterpriseType()).add(saleOrder.getTotalPrice())));
            }
        });
        return enterpriseValueMap;
    }

}
