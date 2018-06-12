package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

@Getter
public class DataViewValue<A, B> {
    A value1;
    B value2;

    public DataViewValue(A value1, B value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}
