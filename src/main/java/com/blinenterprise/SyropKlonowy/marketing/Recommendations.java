package com.blinenterprise.SyropKlonowy.marketing;

import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Recommendations {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;

    public ArrayList<Product> recommendProductsForClientId(Long clientId) {
        ArrayList<Product> results = new ArrayList<Product>();
        saleOrderService.findMostCommonlyPurchasedProducts(clientId).forEach(amountOfProduct ->
                results.add(productService.findById(amountOfProduct.getProductId()).get()));
        return results;
    }

    private Double calculateClientSimilarity(Long firstClientId, Long secondClientId) {
        Double similarity = 1.0;
        Double spendingSimilarity = saleOrderService.findAveragePriceOfProductInClientOrders(firstClientId).divide(
                saleOrderService.findAveragePriceOfProductInClientOrders(secondClientId)).doubleValue();
        Double choiceSimiliarity;
        List<AmountOfProduct> firstClientPurchasedList = saleOrderService.findMostCommonlyPurchasedProducts(firstClientId);

        List<AmountOfProduct> secondClientPurchasedList = saleOrderService.findMostCommonlyPurchasedProducts(secondClientId);
        List<AmountOfProduct> choiceList = firstClientPurchasedList;
        choiceList.retainAll(saleOrderService.findMostCommonlyPurchasedProducts(secondClientId));
        return similarity;
    }
}
