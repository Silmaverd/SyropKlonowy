package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
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
    private Date dateProduction;

    private String description;

    public Product(String name, Double price, Category category, Date dateProduction, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.dateProduction = dateProduction;
        this.description = description;
    }
}
