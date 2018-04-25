package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @Temporal(TemporalType.DATE)
    private Date dateOfOrder;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderedProduct> orderedProducts = new HashSet<>(0);

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderedProduct orderStatus;

    public SaleOrder(Client client, Date dateOfOrder, Set<OrderedProduct> orderedProducts, Double price, OrderedProduct orderStatus) {
        this.client = client;
        this.dateOfOrder = dateOfOrder;
        this.orderedProducts = orderedProducts;
        this.price = price;
        this.orderStatus = orderStatus;
    }
}
