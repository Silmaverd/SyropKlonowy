package com.blinenterprise.SyropKlonowy.domain.Delivery;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @OneToMany
    private List<ProductWithQuantity> listOfProducts;

    public Delivery(List<ProductWithQuantity> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }
}
