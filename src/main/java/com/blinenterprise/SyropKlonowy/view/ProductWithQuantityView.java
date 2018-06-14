package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

import java.util.Date;

@Getter
public class ProductWithQuantityView implements View{
    private String name;
    private String price;
    private Date productionDate;
    private String description;
    private String code;
    private Integer quantity;

    private ProductWithQuantityView(String name, String price, Date productionDate, String description, String code, Integer quantity) {
        this.name = name;
        this.price = price;
        this.productionDate = productionDate;
        this.description = description;
        this.code = code;
        this.quantity = quantity;
    }

    public static ProductWithQuantityView from(String name, String price, Date productionDate, String description, String code, Integer quantity) {
        return new ProductWithQuantityView(name, price, productionDate, description, code, quantity);
    }


}
