package com.blinenterprise.SyropKlonowy.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class AmountOfProduct {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long productId;

    private Integer quantity;

    public AmountOfProduct(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}