package com.blinenterprise.SyropKlonowy.domain;

import com.blinenterprise.SyropKlonowy.domain.Client;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    Set<OrderedProduct> orderedProducts = new HashSet<>(0);

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderedProduct orderStatus;

    public void addOrderedProduct(OrderedProduct orderedProduct){
        orderedProducts.add(orderedProduct);
    }

    public void removeOrderedProduct(OrderedProduct orderedProduct){
        orderedProducts.remove(orderedProduct);
    }
}
