package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderReport;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.view.SaleReportView;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class SaleOrderReportService {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    Environment environment;
    @Autowired
    WarehouseSectorService warehouseSectorService;

    public SaleReportView generateWithinPeriod(Date fromDate, Date toDate) {
        HashMap<Long, Integer> purchasedProductsWithQuantity = new HashMap<Long, Integer>();
        HashMap<Long, Integer> productsRunningOutWithQuantity = new HashMap<Long, Integer>();
        List<AmountOfProduct> notReservedAmountOfProductsInAllSectors = warehouseSectorService.findAllNotReservedAmountsOfProductOnAllSectors();
        ArrayList<SaleOrder> ordersMadeWithinPeriod = Lists.newArrayList(saleOrderService.findAllSaleOrdersBetweenDates(fromDate, toDate));
        ordersMadeWithinPeriod.removeIf(saleOrder -> saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.NEW) ||
                saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.CLOSED));

        ordersMadeWithinPeriod.forEach(saleOrder -> saleOrder.getProductsToOrder().forEach(amountOfProduct ->
                purchasedProductsWithQuantity.put(amountOfProduct.getProductId(), amountOfProduct.getQuantity())));


        notReservedAmountOfProductsInAllSectors.removeIf(amountOfProduct ->
                amountOfProduct.getQuantity() > Integer.parseInt(environment.getProperty("criticalStockAmount")));
        notReservedAmountOfProductsInAllSectors.forEach(amountOfProduct ->
                productsRunningOutWithQuantity.put(amountOfProduct.getProductId(), amountOfProduct.getQuantity()));


        return SaleReportView.from(new SaleOrderReport(
                fromDate,
                toDate,
                purchasedProductsWithQuantity,
                productsRunningOutWithQuantity,
                saleOrderService.findEnterpriseClientsWithOrderVolumeBetweenDates(fromDate, toDate),
                saleOrderService.findEnterpriseClientsWithOrderValueBetweenDates(fromDate, toDate)));
    }
}
