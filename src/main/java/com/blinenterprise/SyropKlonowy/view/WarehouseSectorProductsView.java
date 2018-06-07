package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WarehouseSectorProductsView implements View{
    private String name;
    private String price;
    private String description;
    private Long sectorId;
    private Integer quantity;

    private WarehouseSectorProductsView(String name, String price, String description, Long sectorId, Integer quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.sectorId = sectorId;
    }

    public static WarehouseSectorProductsView from(String name, BigDecimal price, String description, Long sectorId, Integer quantity) {
        return new WarehouseSectorProductsView(name, MoneyConverter.getString(price), description, sectorId, quantity);
    }
}
