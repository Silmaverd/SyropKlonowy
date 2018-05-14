package com.blinenterprise.SyropKlonowy.domain.Delivery;


import com.blinenterprise.SyropKlonowy.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date deliveryDate = new Date();

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToMany
    private List<ProductWithQuantity> listOfProducts;

    public Delivery(List<ProductWithQuantity> listOfProducts) {
        this.deliveryStatus = DeliveryStatus.NEW;
        this.listOfProducts = listOfProducts;
        productsPlacedInDeliveryProcess = new HashMap<>();
    }

    private HashMap<Long, Integer> productsPlacedInDeliveryProcess;

    public ProductWithQuantity getProductWithQuantityForId(Long id) {
        ProductWithQuantity productWithQuantity = null;
        for (ProductWithQuantity product : listOfProducts) {
            if (product.getProduct().getId().equals(id)) productWithQuantity = product;
        }
        return productWithQuantity;
    }

    private boolean isDeliveryFinished(){
        for(ProductWithQuantity productWithQuantity: listOfProducts){
            if (!(productWithQuantity.getQuantity() == productsPlacedInDeliveryProcess.get(productWithQuantity.getProduct().getId())))
                return false;
        }
        return true;
    }

    public void beginDelivering(){
        if (deliveryStatus != DeliveryStatus.NEW)
            throw new IllegalArgumentException();
        deliveryStatus = DeliveryStatus.IN_PROCESS;
        for(ProductWithQuantity productWithQuantity: listOfProducts){
            productsPlacedInDeliveryProcess.put(productWithQuantity.getProduct().getId(), 0);
        }
    }

    public void notifyProductPlacement(Long productWithQuantityId, int amount) {
        ProductWithQuantity productWithQuantity = getProductWithQuantityForId(productWithQuantityId);
        Integer givenProductAmount = productsPlacedInDeliveryProcess.get(productWithQuantityId);
        if ((amount + givenProductAmount > productWithQuantity.getQuantity())
                || (productsPlacedInDeliveryProcess == null))
            throw new IllegalArgumentException();
        productsPlacedInDeliveryProcess.replace(productWithQuantityId, givenProductAmount + amount);
        if (isDeliveryFinished())
            deliveryStatus = DeliveryStatus.DONE;

    }
}
