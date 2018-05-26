package com.blinenterprise.SyropKlonowy.domain.Delivery;

import com.blinenterprise.SyropKlonowy.domain.Product.Product;
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
        if (containsProductWithName(product.getName()))
            getProductWithName(product.getName()).increaseAmountBy(quantity);
        else
            this.listOfProducts.add(new ProductWithQuantity(product, quantity));
        return this;
    }

    public Delivery build(){
        return new Delivery(this.listOfProducts);
    }

    private boolean containsProductWithName(String name){
        for (ProductWithQuantity productWithQuantity: listOfProducts)
            if (name.equals(productWithQuantity.getProduct().getName()))
                return true;
        return false;
    }

    private ProductWithQuantity getProductWithName(String name){
        for (ProductWithQuantity productWithQuantity: listOfProducts)
            if (name.equals(productWithQuantity.getProduct().getName()))
                return productWithQuantity;
        return null;
    }
}
