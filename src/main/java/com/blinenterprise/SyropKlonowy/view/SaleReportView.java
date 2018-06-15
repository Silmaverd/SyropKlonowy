package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderReport;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SaleReportView implements View {

    Date fromDate;
    Date toDate;
    HashMap<Long, Integer> purchasedProductsWithQuantity;
    HashMap<Long, Integer> productsRunningOutWithQuantity;
    Map<Enterprise, Integer> enterpriseWithVolumePurchased;
    Map<Enterprise, BigDecimal> enterpriseWithValuePurchased;


    private SaleReportView(Date fromDate,
                           Date toDate,
                           HashMap<Long, Integer> purchasedProductsWithQuantity,
                           HashMap<Long, Integer> productsRunningOutWithQuantity,
                           Map<Enterprise, Integer> enterpriseWithVolumePurchased,
                           Map<Enterprise, BigDecimal> enterpriseWithValuePurchased) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.purchasedProductsWithQuantity = purchasedProductsWithQuantity;
        this.productsRunningOutWithQuantity = productsRunningOutWithQuantity;
        this.enterpriseWithVolumePurchased = enterpriseWithVolumePurchased;
        this.enterpriseWithValuePurchased = enterpriseWithValuePurchased;
    }

    public static SaleReportView from(SaleOrderReport saleOrderReport) {
        return new SaleReportView(
                saleOrderReport.getFromDate(),
                saleOrderReport.getToDate(),
                saleOrderReport.getPurchasedProductsWithQuantity(),
                saleOrderReport.getProductsRunningOutWithQuantity(),
                saleOrderReport.getEnterpriseWithVolumePurchased(),
                saleOrderReport.getEnterpriseWithValuePurchased()
        );
    }
}
