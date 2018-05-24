package com.blinenterprise.SyropKlonowy.marketing;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;

@Slf4j
@Component
public class Recommendations {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;

    // TODO: Consider client type for recommendations
    // TODO: Add minimum similarity for recommendation
    // TODO: Make sure the results are sorted correctly
    public ArrayList<Product> recommendProductsForClientId(Long clientId) {
        ArrayList<Product> results = new ArrayList<>();
        LinkedHashSet<Long> resultIds = new LinkedHashSet<>();
        ArrayList<Client> clients = new ArrayList<>(clientService.findAll());
        ArrayList<Product> clientProducts = Lists.newArrayList(
                saleOrderService.findAllProductsOrderedByClient(clientId));

        //clients.removeIf(client -> !client.getEnterpriseType().equals(clientService.findById(clientId).get().getEnterpriseType()));
        clients.removeIf(client -> saleOrderService.findAllByClientId(client.getId()).isEmpty());
        clients.removeIf(client -> client.getId().equals(clientId));
        clients.sort(Comparator.comparingDouble(client -> calculateClientSimilarity(clientId, client.getId())));
        clients.removeIf(client -> calculateClientSimilarity(clientId, client.getId()) < 0.5);
        if (clients.size() > 10) clients.subList(10, clients.size()).clear();

        clients.forEach(client ->
                saleOrderService.findMostCommonlyPurchasedProducts(client.getId())
                        .forEach(amountOfProduct ->
                                resultIds.add(amountOfProduct.getProductId())));

        clientProducts.forEach(product ->
                saleOrderService.findFrequentlyBoughtTogether(product.getId())
                        .forEach(amountOfProduct ->
                                resultIds.add(amountOfProduct.getProductId())));


        resultIds.forEach(resultId -> results.add(productService.findById(resultId).get()));
        results.removeIf(product -> clientProducts.contains(product));
        return results;
    }

    private Double calculateClientSimilarity(Long firstClientId, Long secondClientId) {
        Double clientSimilarity = 1.0;
        Double choiceSimilarity = 1.0;
        Double spendingSimilarity = 1.0;

        BigDecimal firstClientAveragePrice = saleOrderService.findAveragePriceOfProductInClientOrders(firstClientId);
        BigDecimal secondClientAveragePrice = saleOrderService.findAveragePriceOfProductInClientOrders(secondClientId);



        ArrayList<Product> firstClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(firstClientId).forEach(amountOfProduct ->
                firstClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));
        ArrayList<Product> secondClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(secondClientId).forEach(amountOfProduct ->
                secondClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));

        ArrayList<Product> choiceIntersectionList = new ArrayList<>(firstClientPurchasedList);
        choiceIntersectionList.retainAll(secondClientPurchasedList);

        if (!secondClientAveragePrice.equals(0)) {
            log.error("Client id " + firstClientId + " average: " + firstClientAveragePrice);
            log.error("Client id " + secondClientId + " average: " + secondClientAveragePrice);
            spendingSimilarity = 1 - firstClientAveragePrice
                    .subtract(secondClientAveragePrice)
                    .abs()
                    .divide(firstClientAveragePrice.add(secondClientAveragePrice), 2, RoundingMode.HALF_EVEN)
                    .doubleValue();

            log.error("Spending similarity: " + spendingSimilarity);
        }

        if (choiceIntersectionList.size() > 0) {
            choiceSimilarity = 1 -
                    ((double) firstClientPurchasedList.size() - (double) choiceIntersectionList.size()) /
                            firstClientPurchasedList.size();
            log.error("Choice similarity: " + choiceSimilarity);
        }

        clientSimilarity = spendingSimilarity * choiceSimilarity;
        return clientSimilarity;
    }
}
