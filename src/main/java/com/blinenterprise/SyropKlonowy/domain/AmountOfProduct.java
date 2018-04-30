package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class AmountOfProduct {

    @Id
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
}