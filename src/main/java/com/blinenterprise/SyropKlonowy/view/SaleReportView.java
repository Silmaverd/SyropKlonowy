package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Getter
public class SaleReportView implements View {

    Date fromDate;
    Date toDate;
    TreeMap<Long, Integer> purchasedProducts;
    List<Product> productsRunningOut;
    Enterprise enterpriseHighestVolumePurchased;
    Enterprise enterpriseHighestValuePurchased;

    public SaleReportView(Date fromDate, Date toDate, TreeMap<Long, Integer> purchasedProducts, List<Product> productsRunningOut, Enterprise enterpriseHighestVolumePurchased,
                          Enterprise enterpriseHighestValuePurchased) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.purchasedProducts = purchasedProducts;
        this.productsRunningOut = productsRunningOut;
        this.enterpriseHighestValuePurchased = enterpriseHighestValuePurchased;
        this.enterpriseHighestVolumePurchased = enterpriseHighestVolumePurchased;
    }

    public static SaleReportView generateWithinPeriod(Date fromDate, Date toDate, SaleOrderService saleOrderService) {
        TreeMap<Long, Integer> purchasedProducts = new TreeMap<Long, Integer>();
        ArrayList<SaleOrder> ordersMadeWithinPeriod = Lists.newArrayList(saleOrderService.findAllSaleOrdersBetweenDates(fromDate, toDate));

        ordersMadeWithinPeriod.removeIf(saleOrder -> saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.NEW) ||
                saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.CLOSED));
        ordersMadeWithinPeriod.forEach(saleOrder -> saleOrder.getProductsToOrder().forEach(amountOfProduct ->
                purchasedProducts.put(amountOfProduct.getProductId(), amountOfProduct.getQuantity())));

        return new SaleReportView(fromDate, toDate, purchasedProducts, null, null, null);
    }
}
