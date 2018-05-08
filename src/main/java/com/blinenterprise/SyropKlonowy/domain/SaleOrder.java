package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long clientId;

    @Temporal(TemporalType.DATE)
    private Date dateOfOrder;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<SaleOrderedProduct> saleOrderedProducts = new ArrayList<>(0);

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private SaleOrderStatus saleOrderStatus;

    public void closeOrder() {
        if (saleOrderStatus == SaleOrderStatus.NEW) {
            saleOrderStatus = SaleOrderStatus.CLOSED;
        } else {
            throw new IllegalStateException();
        }
    }

    public SaleOrder(Long clientId, Date dateOfOrder, List<SaleOrderedProduct> saleOrderedProducts, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.saleOrderedProducts = saleOrderedProducts;
        this.totalPrice = totalPrice;
        this.saleOrderStatus = saleOrderStatus;
    }
}
