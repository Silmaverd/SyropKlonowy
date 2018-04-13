package com.blinenterprise.SyropKlonowy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Temporal(TemporalType.DATE)
    private Date dataProduction;

    private String description;

    public Product(String name, Double price, Category category, Date dataProduction, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.dataProduction = dataProduction;
        this.description = description;
    }
}
