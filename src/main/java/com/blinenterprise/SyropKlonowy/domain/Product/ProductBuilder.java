package com.blinenterprise.SyropKlonowy.domain.Product;

import java.math.BigDecimal;
import java.util.Date;

public final class ProductBuilder {
    private Long id;
    private String name;
    private BigDecimal price;
    private Category category;
    private Date productionDate;
    private String description;

    private ProductBuilder() {
    }

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public ProductBuilder withProductionDate(Date productionDate) {
        this.productionDate = productionDate;
        return this;
    }

    public ProductBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Product build() {
        return new Product(name, price, category, productionDate, description);
    }

    public Product buildWithId() {
        return new Product(id, name, price, category, productionDate, description);
    }
}
