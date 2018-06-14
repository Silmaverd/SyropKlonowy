package com.blinenterprise.SyropKlonowy.domain.WarehouseSector;

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

    public boolean decreaseQuantityBy(Integer quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            return true;
        }
        return false;
    }
}