package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuppliedProduct {
    private Product product;
    private Integer quanity;

    public SuppliedProduct(Product product, Integer quanity) {
        this.product = product;
        this.quanity = quanity;
    }
}
