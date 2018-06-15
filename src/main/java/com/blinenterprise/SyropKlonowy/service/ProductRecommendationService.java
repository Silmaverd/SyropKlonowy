package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

@Service
public class ProductRecommendationService {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;

    public ArrayList<Product> recommendProductsForClientId(Long recommendeeClientId) {
        ArrayList<Product> results = new ArrayList<>();
        HashSet<Long> resultIds = new HashSet<>();
        ArrayList<Product> clientProducts = new ArrayList<>();
        Double similarityThreshold = 0.3;
        Integer recommendationsMinimum = 10;
        Integer recommendationsLimit = 10;


        if (saleOrderService.findAllByClientId(recommendeeClientId).isEmpty()) {
            saleOrderService.findFrequentlyBoughtInLastWeek().forEach(amountOfProduct ->
                    resultIds.add(amountOfProduct.getProductId()));
        } else {
            ArrayList<Client> clients = Lists.newArrayList(clientService.findAll());
            clientProducts = Lists.newArrayList(
                    saleOrderService.findAllProductsOrderedByClient(recommendeeClientId));


            // Filter out clients dissimilar to our recommendee
            clients.removeIf(client -> client.getId().equals(recommendeeClientId));
            clients.removeIf(client -> !client.getEnterpriseType().equals(clientService.findById(recommendeeClientId).get().getEnterpriseType()));
            clients.removeIf(client -> saleOrderService.findAllByClientId(client.getId()).isEmpty());
            clients.sort(Comparator.comparingDouble(client -> calculateClientSimilarity(recommendeeClientId, client.getId())));
            clients.removeIf(client -> calculateClientSimilarity(recommendeeClientId, client.getId()) < similarityThreshold);

            clients.forEach(client ->
                    saleOrderService.findMostCommonlyPurchasedProducts(client.getId())
                            .forEach(amountOfProduct ->
                                    resultIds.add(amountOfProduct.getProductId())));


            // If there aren't enough recommendations based on similar clients
            // Then pad the recommendations based on items frequently bought with
            // things the recommendee has bought in the past
            if (resultIds.size() < recommendationsMinimum) {
                clientProducts
                        .stream()
                        .limit(recommendationsMinimum - resultIds.size())
                        .forEach(product ->
                                saleOrderService.findFrequentlyBoughtTogether(product.getId())
                                        .forEach(amountOfProduct ->
                                                resultIds.add(amountOfProduct.getProductId())));
            }

        }

        resultIds
                .stream()
                .limit(recommendationsLimit)
                .forEach(aLong ->
                        results.add(productService.findById(aLong).get()));
        return results;
    }

    private Double calculateClientSimilarity(Long firstClientId, Long secondClientId) {
        return calculateClientChoiceSimilarity(firstClientId, secondClientId) *
                calculateClientSpendingSimilarity(firstClientId, secondClientId);
    }

    private Double calculateClientChoiceSimilarity(Long firstClientId, Long secondClientId) {
        Double choiceSimilarity = 0.0;

        ArrayList<Product> firstClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(firstClientId).forEach(amountOfProduct ->
                firstClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));
        ArrayList<Product> secondClientPurchasedList = new ArrayList<>();
        saleOrderService.findMostCommonlyPurchasedProducts(secondClientId).forEach(amountOfProduct ->
                secondClientPurchasedList.add(productService.findById(amountOfProduct.getProductId()).get()));

        ArrayList<Product> choiceIntersectionList = new ArrayList<>(firstClientPurchasedList);
        choiceIntersectionList.retainAll(secondClientPurchasedList);

        if (choiceIntersectionList.size() > 0) {
            choiceSimilarity = 1 -
                    ((double) firstClientPurchasedList.size() - (double) choiceIntersectionList.size()) /
                            firstClientPurchasedList.size();
        }

        return choiceSimilarity;
    }

    private Double calculateClientSpendingSimilarity(Long firstClientId, Long secondClientId) {
        Double spendingSimilarity = 0.0;

        BigDecimal firstClientAveragePrice = saleOrderService.findAveragePriceOfProductInClientOrders(firstClientId);
        BigDecimal secondClientAveragePrice = saleOrderService.findAveragePriceOfProductInClientOrders(secondClientId);


        if (!firstClientAveragePrice.equals(0) && !secondClientAveragePrice.equals(0)) {
            spendingSimilarity = 1 - firstClientAveragePrice
                    .subtract(secondClientAveragePrice)
                    .abs()
                    .divide(firstClientAveragePrice.add(secondClientAveragePrice), 2, RoundingMode.HALF_EVEN)
                    .doubleValue();
        }

        return spendingSimilarity;
    }
}
