package com.blinenterprise.SyropKlonowy.domain.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Temporal(TemporalType.DATE)
    private Date productionDate;

    private String description;

    public Product(String name, BigDecimal price, Category category, Date productionDate, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
    }

    public Product(Long id, String name, BigDecimal price, Category category, Date productionDate, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
    }
}
