package com.blinenterprise.SyropKlonowy.domain.SaleOrder;

import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class SaleOrderReport {
    Date fromDate;
    Date toDate;
    HashMap<Long, Integer> purchasedProductsWithQuantity;
    HashMap<Long, Integer> productsRunningOutWithQuantity;
    Map<Enterprise, Integer> enterpriseWithVolumePurchased;
    Map<Enterprise, BigDecimal> enterpriseWithValuePurchased;
}
