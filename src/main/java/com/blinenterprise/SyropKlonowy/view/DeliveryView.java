package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@Getter
public class DeliveryView implements View {

    private Long id;
    private Long destinationWarehouseId;
    private Date deliveryDate;
    private List<ProductWithQuantity> listOfProducts;

    public static DeliveryView from(Delivery delivery){
        return new DeliveryView(
                delivery.getId(),
                delivery.getTargetWarehouseId(),
                delivery.getDeliveryDate(),
                delivery.getListOfProducts());
    }

    public DeliveryView(Long id, Long destinationWarehouseId, Date deliveryDate, List<ProductWithQuantity> listOfProducts) {
        this.id = id;
        this.destinationWarehouseId = destinationWarehouseId;
        this.deliveryDate = deliveryDate;
        this.listOfProducts = listOfProducts;
    }
}
