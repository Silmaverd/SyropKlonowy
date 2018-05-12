package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
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
    private Date deliveryDate;
    private List<ProductWithQuantity> listOfProducts;
    private DeliveryStatus deliveryStatus;

    public static DeliveryView from(Delivery delivery){
        return new DeliveryView(
                delivery.getId(),
                delivery.getDeliveryDate(),
                delivery.getListOfProducts(),
                delivery.deliveryStatus);
    }

    public DeliveryView(Long id, Date deliveryDate, List<ProductWithQuantity> listOfProducts, DeliveryStatus deliveryStatus) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.listOfProducts = listOfProducts;
        this.deliveryStatus = deliveryStatus;
    }
}
