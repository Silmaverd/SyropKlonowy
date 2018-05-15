package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
public class WarehouseSector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer currentAmountOfProducts;
    private Integer maxAmountOfProducts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> amountOfProducts = new HashMap<>();

    public boolean addAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (!isPossibleToAddNewProducts(quantityOfProduct)) {
            return false;
        }
        if (amountOfProducts.containsKey(productId)) {
            amountOfProducts.get(productId).increaseQuantityBy(quantityOfProduct);
        } else {
            amountOfProducts.put(productId, amountOfProduct);
        }
        currentAmountOfProducts += quantityOfProduct;
        return true;
    }

    public boolean removeAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (!isPossibleToRemoveProducts(quantityOfProduct)) {
            return false;
        }
        if (amountOfProducts.containsKey(productId)) {
            if (amountOfProducts.get(productId).decreaseQuantityBy(quantityOfProduct)) {
                currentAmountOfProducts -= quantityOfProduct;
                return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public WarehouseSector(String name, Integer maxAmountOfProducts) {
        this.name = name;
        this.maxAmountOfProducts = maxAmountOfProducts;
        this.currentAmountOfProducts = 0;
    }

    public boolean isPossibleToAddNewProducts(Integer amountOfNewProduct) {
        return amountOfNewProduct + currentAmountOfProducts <= maxAmountOfProducts;
    }

    public boolean isPossibleToRemoveProducts(Integer amountOfProducts) {
        return currentAmountOfProducts - amountOfProducts >= 0;
    }
}
