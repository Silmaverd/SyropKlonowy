package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderedProduct;
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
    List<SaleOrderedProduct> saleOrderedProducts;
    private BigDecimal totalPrice;
    private SaleOrderStatus saleOrderStatus;

    public SaleOrderView(Long id, Long clientId, Date dateOfOrder, List<SaleOrderedProduct> saleOrderedProducts, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.id = id;
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.saleOrderedProducts = saleOrderedProducts;
        this.totalPrice = totalPrice;
        this.saleOrderStatus = saleOrderStatus;
    }
}
