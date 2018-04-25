package com.blinenterprise.SyropKlonowy.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class ProductLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long productId;

    private Integer quantity;

    public ProductLine(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
