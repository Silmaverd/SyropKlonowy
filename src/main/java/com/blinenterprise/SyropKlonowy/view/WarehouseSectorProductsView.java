package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WarehouseSectorProductsView implements View{
    private Long productId;
    private String name;
    private String price;
    private String category;
    private String description;
    private Long sectorId;
    private Integer quantity;

    private WarehouseSectorProductsView(Long productId, String name, String price, String category, String description, Long sectorId, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.sectorId = sectorId;
    }

    public static WarehouseSectorProductsView from(Long productId, String name, BigDecimal price, Category category, String description, Long sectorId, Integer quantity) {
        return new WarehouseSectorProductsView(productId, name, MoneyConverter.getString(price), category.toString(), description, sectorId, quantity);
    }
}
