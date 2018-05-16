package com.blinenterprise.SyropKlonowy.domain;

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

    private Integer currentAmountOfProducts;
    private Integer maxAmountOfProducts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> amountOfProducts = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> saleOrderedAmountOfProducts = new HashMap<>();

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
                unReserveAmountOfProductsFromSaleOrder(amountOfProduct);
                currentAmountOfProducts -= quantityOfProduct;
                return true;
            }
        }
        return false;
    }

    public boolean reserveSaleOrderedAmountOfProducs(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (isPossibleToRemoveProducts(quantityOfProduct)) {
            if (amountOfProducts.containsKey(productId)) {
                addSaleOrderedProduct(amountOfProduct);
                return true;
            }
        }
        return false;
    }

    private void addSaleOrderedProduct(AmountOfProduct orderedAmountOfProduct) {
        Long productId = orderedAmountOfProduct.getProductId();
        Integer quantityOfProduct = orderedAmountOfProduct.getQuantity();
        if (saleOrderedAmountOfProducts.containsKey(productId)) {
            saleOrderedAmountOfProducts.get(productId).increaseQuantityBy(quantityOfProduct);
        } else {
            saleOrderedAmountOfProducts.put(productId, orderedAmountOfProduct);
        }
    }

    public WarehouseSector(String name, Integer maxAmountOfProducts) {
        this.name = name;
        this.maxAmountOfProducts = maxAmountOfProducts;
        this.currentAmountOfProducts = 0;
    }

    public WarehouseSector(Long id, String name, Integer maxAmountOfProducts) {
        this.id = id;
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

    public Integer getQuantityOfProductByIdIfExist(Long productId) {
        if (amountOfProducts.containsKey(productId)) {
            return amountOfProducts.get(productId).getQuantity();
        }
        return 0;
    }

    public Integer getSaleOrderQuantityOfProductByIdIfExist(Long productId) {
        if (saleOrderedAmountOfProducts.containsKey(productId)) {
            return saleOrderedAmountOfProducts.get(productId).getQuantity();
        }
        return 0;
    }

    public void unReserveAmountOfProductsFromSaleOrder(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        Integer quantityOfProduct = amountOfProduct.getQuantity();
        if (saleOrderedAmountOfProducts.containsKey(productId)) {
            saleOrderedAmountOfProducts.get(productId).decreaseQuantityBy(quantityOfProduct);
            if (saleOrderedAmountOfProducts.get(productId).getQuantity() == 0) {
                saleOrderedAmountOfProducts.remove(productId);
            }
        }
    }
}
