package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class DeliveryView implements View {

    private Long id;
    private Date deliveryDate;
    private List<ProductWithQuantityView> listOfProducts;
    private DeliveryStatus deliveryStatus;
    public static DeliveryView from(Delivery delivery){
        return new DeliveryView(
                delivery.getId(),
                delivery.getDeliveryDate(),
                delivery.getListOfProducts(),
                delivery.getDeliveryStatus());
    }

    public DeliveryView(Long id, Date deliveryDate, List<ProductWithQuantity> listOfProducts, DeliveryStatus deliveryStatus) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.listOfProducts = listOfProducts.stream().map(productWithQuantity -> ProductWithQuantityView.from(
                productWithQuantity.getProduct().getId(),
                productWithQuantity.getProduct().getName(),
                MoneyConverter.getString(productWithQuantity.getProduct().getPrice()),
                productWithQuantity.getProduct().getCategory().toString(),
                productWithQuantity.getProduct().getProductionDate(),
                productWithQuantity.getProduct().getDescription(),
                productWithQuantity.getQuantity()
        )).collect(Collectors.toList());
        this.deliveryStatus = deliveryStatus;
    }
}
