package com.blinenterprise.SyropKlonowy.domain.SaleOrder;

import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public class SaleOrderReport {
    Date fromDate;
    Date toDate;
    TreeMap<Long, Integer> purchasedProductsWithQuantity;
    TreeMap<Long, Integer> productsRunningOutWithQuantity;
    Map<Enterprise, Integer> enterpriseWithVolumePurchased;
    Map<Enterprise, BigDecimal> enterpriseWithValuePurchased;
}
