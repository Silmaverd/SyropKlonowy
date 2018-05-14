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

    private final Integer maxAmountOfProducts = 50;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> amountOfProducts = new HashMap<>();

    public void addAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        if (amountOfProducts.containsKey(productId)) {
            amountOfProducts.get(productId).increaseQuantityBy(amountOfProduct.getQuantity());
        } else {
            amountOfProducts.put(productId, amountOfProduct);
        }
    }

    public void removeAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        if (amountOfProducts.containsKey(productId)) {
            amountOfProducts.get(productId).decreaseQuantityBy(amountOfProduct.getQuantity());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public WarehouseSector(String name) {
        this.name = name;
    }

    public boolean isPossibleToAddNewProducts(Integer amountOfNewProduct){
        return amountOfNewProduct <= maxAmountOfProducts;
    }
}
