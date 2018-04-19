package com.blinenterprise.SyropKlonowy.domain.Order;

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
    Set<OrderedProduct> orderedProducts;

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Client client, Date orderDate, Double price, OrderStatus orderStatus) {
        this.client = client;
        this.orderDate = orderDate;
        this.orderedProducts = new HashSet<>();
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public void addOrderedProduct(OrderedProduct orderedProduct){
        orderedProducts.add(orderedProduct);
    }

    public void removeOrderedProduct(OrderedProduct orderedProduct){
        orderedProducts.remove(orderedProduct);
    }
}
