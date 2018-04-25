package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, AmountOfProduct> amountOfProducts = new HashMap<>();

    public Warehouse(Map<Long, AmountOfProduct> amountOfProducts) {
        this.amountOfProducts = amountOfProducts;
    }

    public void addAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        if (amountOfProducts.containsKey(productId)) {
            amountOfProducts.get(productId).increasedQuantityBy(amountOfProduct.getQuantity());
            return;
        }
        amountOfProducts.put(productId, amountOfProduct);
    }

    public void removeAmountOfProduct(AmountOfProduct amountOfProduct) {
        Long productId = amountOfProduct.getProductId();
        if (amountOfProducts.containsKey(productId)) {
            amountOfProducts.get(productId).descreaseQuantityBy(amountOfProduct.getQuantity());
            return;
        }
        throw new IllegalArgumentException();
    }
}
