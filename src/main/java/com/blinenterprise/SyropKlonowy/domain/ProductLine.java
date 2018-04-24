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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Product product;

    private Integer quantity;

    public ProductLine(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
