package com.blinenterprise.SyropKlonowy.view;


import com.blinenterprise.SyropKlonowy.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@Entity
@NoArgsConstructor
@Immutable
public class ProductView {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Temporal(TemporalType.DATE)
    private Date productionDate;

    private String description;

    public ProductView(String name, Double price, Category category, Date productionDate, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
    }

}