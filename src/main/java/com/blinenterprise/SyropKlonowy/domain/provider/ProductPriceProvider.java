package com.blinenterprise.SyropKlonowy.domain.provider;

import com.blinenterprise.SyropKlonowy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class ProductPriceProvider {
    @Autowired
    private ProductService productService;
    private static ProductPriceProvider productPriceProvider = new ProductPriceProvider();

    private ProductPriceProvider() {
    }

    ;

    public ProductPriceProvider getInstance() {
        return this;
    }

    public BigDecimal getProductPriceById(Long productId) {
        return productService.findById(productId).get().getPrice();
    }
}
