package com.blinenterprise.SyropKlonowy.domain.Delivery.processing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class AmountLeftToDeliver {
    public String productName;
    public Long productId;
    public Integer amount;
}
