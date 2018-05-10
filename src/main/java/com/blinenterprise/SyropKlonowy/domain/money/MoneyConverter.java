package com.blinenterprise.SyropKlonowy.domain.money;


import java.math.BigDecimal;

public class MoneyConverter {

    public static BigDecimal getBigDecimal(Integer price1, Integer price2){
        return new BigDecimal(price1*100+price2);
    }

    public static String getString(BigDecimal price){
        return price.divide(new BigDecimal(100))+" PLN";
    }

}
