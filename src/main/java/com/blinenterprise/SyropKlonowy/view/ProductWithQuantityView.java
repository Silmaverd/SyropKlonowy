package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

import java.util.Date;

@Getter
public class ProductWithQuantityView implements View{
    private Long productId;
    private String name;
    private String price;
    private String category;
    private Date productionDate;
    private String description;
    private Integer quantity;

    private ProductWithQuantityView(Long productId, String name, String price, String category, Date productionDate, String description, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
        this.quantity = quantity;
    }

    public static ProductWithQuantityView from(Long productId, String name, String price, String category, Date productionDate, String description, Integer quantity) {
        return new ProductWithQuantityView(productId, name, price, category, productionDate, description, quantity);
    }


}
