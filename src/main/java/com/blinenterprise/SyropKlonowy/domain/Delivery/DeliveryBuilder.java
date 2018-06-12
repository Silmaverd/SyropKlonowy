package com.blinenterprise.SyropKlonowy.domain.Delivery;

import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Getter
public final class DeliveryBuilder {

    private List<ProductWithQuantity> listOfProducts;

    private DeliveryStatus deliveryStatus;

    public DeliveryBuilder(){
        this.listOfProducts = new LinkedList<>();
        this.deliveryStatus = DeliveryStatus.TEMPLATE;
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

    public DeliveryBuilder removeQuantityOfProduct(String productName, int quantity) {
        if (containsProductWithName(productName)){
            ProductWithQuantity productToReduce = getProductWithName(productName);
            if (quantity == productToReduce.getQuantity() || !productToReduce.decreaseAmountBy(quantity)){
                listOfProducts.remove(productToReduce);
            }
        }
        return this;
    }

    public Delivery build(){
        return new Delivery(this.listOfProducts, DeliveryStatus.NEW);
    }

    public Delivery getTemplate(){ return new Delivery(this.listOfProducts, DeliveryStatus.TEMPLATE); }

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
