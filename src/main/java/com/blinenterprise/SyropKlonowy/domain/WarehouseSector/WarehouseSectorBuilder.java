package com.blinenterprise.SyropKlonowy.domain.WarehouseSector;

public final class WarehouseSectorBuilder {
    private Long id;
    private String name;
    private Integer maxAmountOfProducts;

    private WarehouseSectorBuilder() {
    }

    public static WarehouseSectorBuilder aWarehouseSector() {
        return new WarehouseSectorBuilder();
    }

    public WarehouseSectorBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WarehouseSectorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public WarehouseSectorBuilder withMaxAmountOfProducts(Integer maxAmountOfProducts) {
        this.maxAmountOfProducts = maxAmountOfProducts;
        return this;
    }

    public WarehouseSector build() {
        return new WarehouseSector(id, name, maxAmountOfProducts);
    }
}
