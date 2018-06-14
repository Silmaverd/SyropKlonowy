package com.blinenterprise.SyropKlonowy.view.status;

import com.blinenterprise.SyropKlonowy.view.View;
import lombok.Getter;

@Getter
public class NumberView implements View{

    private Integer number;

    private NumberView(Integer number) {
        this.number = number;
    }

    public static NumberView from(Integer number) {
        return new NumberView(number);
    }
}
