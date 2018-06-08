package com.blinenterprise.SyropKlonowy.view.status;

import com.blinenterprise.SyropKlonowy.view.View;
import lombok.Getter;

@Getter
public class ProductsSoldView implements View{

    private Integer amountOfProductsSold;

    private ProductsSoldView(Integer amountOfProductsSold) {
        this.amountOfProductsSold = amountOfProductsSold;
    }

    public static ProductsSoldView from(Integer amountOfProductsSold) {
        return new ProductsSoldView(amountOfProductsSold);
    }
}
