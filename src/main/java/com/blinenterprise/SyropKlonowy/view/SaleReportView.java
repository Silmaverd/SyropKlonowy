package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public class SaleReportView implements View {

    Date fromDate;
    Date toDate;
    TreeMap<Long, Integer> purchasedProductsWithQuantity;
    TreeMap<Long, Integer> productsRunningOutWithQuantity;
    Map<Enterprise, Integer> enterpriseWithVolumePurchased;
    Map<Enterprise, BigDecimal> enterpriseWithValuePurchased;


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
