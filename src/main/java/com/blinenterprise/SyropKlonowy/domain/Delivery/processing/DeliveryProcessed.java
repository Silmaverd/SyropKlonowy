package com.blinenterprise.SyropKlonowy.domain.Delivery.processing;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor
@Getter
public class DeliveryProcessed{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private Long deliveryId;

    @OneToMany
    private List<ProductWithQuantity> listOfProductsToDeliver;

    public DeliveryProcessed(List<ProductWithQuantity> listOfProducts, Long deliveryId) {
        this.deliveryId = deliveryId;
        this.listOfProductsToDeliver = listOfProducts;
    }

    public void decreaseQuantityOfProduct(Long productId, int amount){
        ProductWithQuantity productWithQuantity = getProductWithId(productId).orElseThrow(IllegalArgumentException::new);
        productWithQuantity.decreaseAmountBy(amount);
        if (productWithQuantity.getQuantity() == 0)
            listOfProductsToDeliver.remove(productWithQuantity);
    }

    public boolean isDeliveryFinished(){
        return listOfProductsToDeliver.isEmpty();
    }

    private Optional<ProductWithQuantity> getProductWithId(Long id){
        for(ProductWithQuantity p: listOfProductsToDeliver){
            if (p.getId().equals(id)) return Optional.of(p);
        }
        return Optional.empty();
    }
}
