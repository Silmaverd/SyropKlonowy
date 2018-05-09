package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Getter
public class SaleOrderView implements View {

    private Long id;
    private Long clientId;
    private Date dateOfOrder;
    List<ProductWithQuantity> productsWithQuantities;
    private BigDecimal totalPrice;
    private SaleOrderStatus saleOrderStatus;

    private SaleOrderView(Long id, Long clientId, Date dateOfOrder, List<ProductWithQuantity> productsWithQuantities, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.id = id;
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.productsWithQuantities = productsWithQuantities;
        this.totalPrice = totalPrice;
        this.saleOrderStatus = saleOrderStatus;
    }

    public static SaleOrderView from(SaleOrder saleOrder) {
        return new SaleOrderView(
                saleOrder.getId(),
                saleOrder.getClientId(),
                saleOrder.getDateOfOrder(),
                saleOrder.getProductsWithQuantities(),
                saleOrder.getTotalPrice(),
                saleOrder.getSaleOrderStatus());
    }
}
