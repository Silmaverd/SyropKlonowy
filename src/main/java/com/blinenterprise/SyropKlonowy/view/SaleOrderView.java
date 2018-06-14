package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
public class SaleOrderView implements View {

    private Long id;
    private Long clientId;
    private Date dateOfOrder;
    List<AmountOfProduct> amountsOfProducts;
    private String totalPrice;
    private SaleOrderStatus saleOrderStatus;

    private SaleOrderView(Long id, Long clientId, Date dateOfOrder, List<AmountOfProduct> amountsOfProducts, BigDecimal totalPrice, SaleOrderStatus saleOrderStatus) {
        this.id = id;
        this.clientId = clientId;
        this.dateOfOrder = dateOfOrder;
        this.amountsOfProducts = amountsOfProducts;
        this.totalPrice = MoneyConverter.getString(totalPrice);
        this.saleOrderStatus = saleOrderStatus;
    }

    public static SaleOrderView from(SaleOrder saleOrder) {
        return new SaleOrderView(
                saleOrder.getId(),
                saleOrder.getClientId(),
                saleOrder.getDateOfOrder(),
                saleOrder.getProductsToOrder(),
                saleOrder.getTotalPrice(),
                saleOrder.getSaleOrderStatus());
    }
}
