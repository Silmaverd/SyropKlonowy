package com.blinenterprise.SyropKlonowy.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

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

    private ArrayList<ProductWithQuantity> listOfProducts;

    public Delivery(ArrayList<ProductWithQuantity> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }
}
