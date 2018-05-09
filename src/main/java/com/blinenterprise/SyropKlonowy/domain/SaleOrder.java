package com.blinenterprise.SyropKlonowy.domain;

import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
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
    List<ProductWithQuantity> productsWithQuantities = new ArrayList<>(0);

    @Setter
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private SaleOrderStatus saleOrderStatus;

    public void closeOrder() {
        if (saleOrderStatus == SaleOrderStatus.NEW) {
            saleOrderStatus = SaleOrderStatus.CLOSED;
        } else {
            log.error("Could not close order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.NEW);
        }
    }

    public void payOrder() {
        if (saleOrderStatus == SaleOrderStatus.NEW) {
            saleOrderStatus = SaleOrderStatus.PAID;
        } else {
            log.error("Could not close order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.NEW);
        }
    }

    public void sendOrder() {
        if (saleOrderStatus == SaleOrderStatus.PAID) {
            saleOrderStatus = SaleOrderStatus.SENT;
        } else {
            log.error("Could not close order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.PAID);
        }
    }

    public boolean addProductWithQuantity(ProductWithQuantity productWithQuantity) {
        return productsWithQuantities.add(productWithQuantity);
    }

    public SaleOrder(Long clientId, Date dateOfOrder, List<ProductWithQuantity> productsWithQuantities, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.productsWithQuantities = productsWithQuantities;
        this.totalPrice = totalPrice;
        this.saleOrderStatus = saleOrderStatus;
    }
}
