package com.blinenterprise.SyropKlonowy.domain.SaleOrder;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    List<AmountOfProduct> amountsOfProducts = new ArrayList<>(0);

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private SaleOrderStatus saleOrderStatus;

    public boolean closeOrder() {
        if (saleOrderStatus == SaleOrderStatus.NEW) {
            saleOrderStatus = SaleOrderStatus.CLOSED;
            return true;
        } else {
            log.error("Could not close order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.NEW);
            return false;
        }
    }

    public boolean payOrder() {
        if (saleOrderStatus == SaleOrderStatus.NEW) {
            saleOrderStatus = SaleOrderStatus.PAID;
            return true;
        } else {
            log.error("Could not pay order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.NEW);
            return false;
        }
    }

    public boolean sendOrder() {
        if (saleOrderStatus == SaleOrderStatus.PAID) {
            saleOrderStatus = SaleOrderStatus.SENT;
            return true;
        } else {
            log.error("Could not send order, current order status is " + saleOrderStatus + ", must be "
                    + SaleOrderStatus.PAID);
            return false;
        }
    }

    public void recalculateTotalPrice(ProductService productService) {
        totalPrice = new BigDecimal(0);
        log.debug("Successfully retrieved productPriceProvider instance.");
        amountsOfProducts.forEach(amountOfProduct ->
                totalPrice = totalPrice.add(productService.findById(
                        amountOfProduct.getProductId()).get().getPrice().multiply(BigDecimal.valueOf(amountOfProduct.getQuantity()))));
        log.debug("Successfully recalculated the price");
    }

    public boolean addAmountOfProduct(AmountOfProduct amountOfProduct) {
        log.debug("Successfully entered the addAmountOfProduct() method");
        return amountsOfProducts.add(amountOfProduct);
    }

    public SaleOrder(Long clientId, Date dateOfOrder, List<AmountOfProduct> amountsOfProducts, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.amountsOfProducts = amountsOfProducts;
        this.totalPrice = totalPrice;
        this.saleOrderStatus = saleOrderStatus;
    }
}
