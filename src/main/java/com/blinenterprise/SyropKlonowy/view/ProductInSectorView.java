package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

@Getter
public class ProductInSectorView implements View{
    private Long warehouseSectorId;
    private Long productId;
    private Integer productQuantity;

    private ProductInSectorView(Long warehouseSectorId, Long productId, Integer productQuantity) {
        this.warehouseSectorId = warehouseSectorId;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public static ProductInSectorView from(Long warehouseSectorId, Long productId, Integer productQuantity){
        return new ProductInSectorView(warehouseSectorId, productId, productQuantity);
    }
}
