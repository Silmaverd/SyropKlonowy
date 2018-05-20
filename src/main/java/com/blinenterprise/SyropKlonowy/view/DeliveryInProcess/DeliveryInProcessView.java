package com.blinenterprise.SyropKlonowy.view.DeliveryInProcess;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.view.View;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class DeliveryInProcessView implements View {

    private Long deliveryId;
    private List<AmountLeftToDeliver> productsLeftForDelivery;

    private DeliveryInProcessView(Long deliveryId, List productsLeft) {
        this.deliveryId = deliveryId;
        this.productsLeftForDelivery = productsLeft;
    }

    public static DeliveryInProcessView from(Delivery delivery) {
        if (delivery.getDeliveryStatus() != DeliveryStatus.IN_PROCESS) return null;
        ArrayList productsLeft = new ArrayList();
        delivery.getProductsPlacedInDeliveryProcess().forEach((id, amount) -> {
            ProductWithQuantity productWithQuantity = delivery.getProductWithQuantityForId(id);
            if (amount != productWithQuantity.getQuantity())
                productsLeft.add(new AmountLeftToDeliver(productWithQuantity.getProduct().getName(),
                        productWithQuantity.getProduct().getId(),
                        productWithQuantity.getQuantity() - amount));
        });
        return new DeliveryInProcessView(delivery.getId(), productsLeft);
    }
}
