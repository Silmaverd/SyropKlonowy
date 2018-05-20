package com.blinenterprise.SyropKlonowy.domain.WarehouseSector;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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

    private Integer maxAmountOfProducts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> notReservedAmountOfProducts = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> reservedAmountOfProducts = new HashMap<>();

    public boolean addAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (isPossibleToAddNewProducts(quantityOfProduct)) {
            if (notReservedAmountOfProducts.containsKey(productId)) {
                notReservedAmountOfProducts.get(productId).increaseQuantityBy(quantityOfProduct);
            } else {
                notReservedAmountOfProducts.put(productId, amountOfProduct);
            }
            return true;
        }
        return false;
    }

    public boolean removeReservedAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (reservedAmountOfProducts.containsKey(productId) &&
                isPossibleToRemoveProducts(quantityOfProduct) &&
                reservedAmountOfProducts.get(productId).decreaseQuantityBy(quantityOfProduct)) {

            if (reservedAmountOfProducts.get(productId).getQuantity() == 0) {
                reservedAmountOfProducts.remove(productId);
            }
            return true;
        }
        return false;
    }

    public boolean removeAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (notReservedAmountOfProducts.containsKey(productId) &&
                isPossibleToRemoveProducts(quantityOfProduct) &&
                notReservedAmountOfProducts.get(productId).decreaseQuantityBy(quantityOfProduct)) {

            if (notReservedAmountOfProducts.get(productId).getQuantity() == 0) {
                notReservedAmountOfProducts.remove(productId);
            }
            return true;
        }
        return false;
    }

    public boolean reserveAmountOfProduct(AmountOfProduct amountOfProduct) {
        if (removeAmountOfProduct(amountOfProduct)) {
            addReservedAmountOfProduct(amountOfProduct);
            return true;
        }
        return false;
    }

    private void addReservedAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (reservedAmountOfProducts.containsKey(productId)) {
            reservedAmountOfProducts.get(productId).increaseQuantityBy(quantityOfProduct);
        } else {
            reservedAmountOfProducts.put(productId, amountOfProduct);
        }
    }

    public WarehouseSector(String name, Integer maxAmountOfProducts) {
        this.name = name;
        this.maxAmountOfProducts = maxAmountOfProducts;
    }

    public WarehouseSector(Long id, String name, Integer maxAmountOfProducts) {
        this.id = id;
        this.name = name;
        this.maxAmountOfProducts = maxAmountOfProducts;
    }

    public Integer getCurrentAmountOfProducts() {
        Integer currentAmountOfProducts = notReservedAmountOfProducts.values()
                .stream()
                .mapToInt(AmountOfProduct::getQuantity)
                .sum();
        Integer currentSaleOrderedAmountOfProducts = reservedAmountOfProducts.values()
                .stream()
                .mapToInt(AmountOfProduct::getQuantity)
                .sum();
        return currentAmountOfProducts + currentSaleOrderedAmountOfProducts;
    }

    public boolean isPossibleToAddNewProducts(int amountOfNewProduct) {
        return amountOfNewProduct + getCurrentAmountOfProducts() <= maxAmountOfProducts;
    }

    public boolean isPossibleToRemoveProducts(int amountOfProducts) {
        return getCurrentAmountOfProducts() - amountOfProducts >= 0;
    }

    public Integer getQuantityOfNotReservedProductByIdIfExist(Long productId) {
        if (notReservedAmountOfProducts.containsKey(productId)) {
            return notReservedAmountOfProducts.get(productId).getQuantity();
        }
        return 0;
    }

    public Integer getQuantityOfReservedProductByIdIfExist(Long productId) {
        if (reservedAmountOfProducts.containsKey(productId)) {
            return reservedAmountOfProducts.get(productId).getQuantity();
        }
        return 0;
    }

    public boolean unReserveAmountOfProduct(AmountOfProduct amountOfProduct) {
        if (removeReservedAmountOfProduct(amountOfProduct)) {
            addAmountOfProduct(amountOfProduct);
            return true;
        }
        return false;
    }
}

