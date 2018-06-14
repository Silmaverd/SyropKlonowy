package com.blinenterprise.SyropKlonowy.view.status;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.view.View;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceView implements View {
    private String price;

    private PriceView(String price) {
        this.price = price;
    }

    public static PriceView from(BigDecimal price) {
        return new PriceView(MoneyConverter.getString(price));
    }
}
