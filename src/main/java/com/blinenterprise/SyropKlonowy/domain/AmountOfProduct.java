package com.blinenterprise.SyropKlonowy.domain;

import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class AmountOfProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long productId;
    private Integer quantity;

    public AmountOfProduct(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void increaseQuantityBy(Integer quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantityBy(Integer quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static AmountOfProduct fromProductWithQuantity(ProductWithQuantity productWithQuantity){
        return new AmountOfProduct(productWithQuantity.getId(), productWithQuantity.getQuantity());
    }
}