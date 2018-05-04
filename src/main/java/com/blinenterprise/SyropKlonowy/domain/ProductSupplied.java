package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSupplied {
    private Product product;
    private Integer quanity;

    public ProductSupplied(Product product, Integer quanity) {
        this.product = product;
        this.quanity = quanity;
    }
}
