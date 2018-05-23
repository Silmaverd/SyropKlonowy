package com.blinenterprise.SyropKlonowy.marketing;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class Recommendations {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;

    public ArrayList<Product> recommendProductsForClientId(Long clientId) {
        ArrayList<Product> results = new ArrayList<>();
        HashSet<Long> resultIds = new HashSet<>();
        ArrayList<Client> clients = new ArrayList(clientService.findAll());
        ArrayList<Product> products = new ArrayList(productService.findAll());

        clients.sort((Client c1, Client c2) -> Double.compare(
                calculateClientSimilarity(c1.getId(), clientId),
                calculateClientSimilarity(c2.getId(), clientId)));

        if (clients.size() > 10) {
            clients.subList(10, clients.size()).clear();
        }
        clients.forEach(client ->
                saleOrderService.findMostCommonlyPurchasedProducts(client.getId()).forEach(
                        amountOfProduct -> resultIds.add(amountOfProduct.getProductId())));

        return results;
    }

    private Double calculateClientSimilarity(Long firstClientId, Long secondClientId) {
        Double clientSimilarity = 1.0;
        Double spendingSimilarity = saleOrderService.findAveragePriceOfProductInClientOrders(firstClientId).divide(
                saleOrderService.findAveragePriceOfProductInClientOrders(secondClientId)).doubleValue();

        ArrayList<Product> firstClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(firstClientId).forEach(amountOfProduct ->
                firstClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));
        ArrayList<Product> secondClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(secondClientId).forEach(amountOfProduct ->
                secondClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));
        ArrayList<Product> choiceIntersectionList = new ArrayList<>(firstClientPurchasedList);
        choiceIntersectionList.retainAll(secondClientPurchasedList);

        Double choiceSimilarity = Double.valueOf(firstClientPurchasedList.size() / choiceIntersectionList.size() *
                secondClientPurchasedList.size() / choiceIntersectionList.size());

        clientSimilarity = spendingSimilarity * choiceSimilarity;
        return clientSimilarity;
    }
}
