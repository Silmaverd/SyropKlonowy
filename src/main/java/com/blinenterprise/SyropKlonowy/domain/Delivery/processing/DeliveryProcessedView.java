package com.blinenterprise.SyropKlonowy.domain.Delivery.processing;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.web.View;
import lombok.AllArgsConstructor;

import java.util.List;

public class DeliveryProcessedView implements View {

    private List<ProductWithQuantity> productsToDeliver;
    private Long deliveryId;

    private DeliveryProcessedView(List<ProductWithQuantity> productsToDeliver, Long deliveryId) {
        this.productsToDeliver = productsToDeliver;
        this.deliveryId = deliveryId;
    }

    public static DeliveryProcessedView from(List<ProductWithQuantity> productsToDeliver, Long deliveryId){
        return new DeliveryProcessedView(productsToDeliver, deliveryId);
    }
}
