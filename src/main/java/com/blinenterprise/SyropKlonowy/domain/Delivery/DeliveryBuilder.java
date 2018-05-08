package com.blinenterprise.SyropKlonowy.domain.Delivery;

import com.blinenterprise.SyropKlonowy.domain.Product;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public final class DeliveryBuilder {

    private List<ProductWithQuantity> listOfProducts;

    public DeliveryBuilder(){
        this.listOfProducts = new LinkedList<>();
    }

    public static DeliveryBuilder aDelivery(){
        return new DeliveryBuilder();
    }

    public DeliveryBuilder addProduct(Product product, int quantity){
        this.listOfProducts.add(new ProductWithQuantity(product, quantity));
        return this;
    }

    public Delivery build(Long destinationWarehouseId){
        return new Delivery(this.listOfProducts, destinationWarehouseId);
    }
}
