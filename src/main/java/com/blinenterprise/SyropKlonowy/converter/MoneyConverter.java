package com.blinenterprise.SyropKlonowy.converter;


import java.math.BigDecimal;

public class MoneyConverter {

    public static BigDecimal getBigDecimal(String price){
        price = price.replace("PLN", "");
        String[] prices = price.split(",|\\.");

        return new BigDecimal(Integer.valueOf(prices[0])*100+Integer.valueOf(prices[1]));
    }

    public static String getString(BigDecimal price){
        return price.divide(new BigDecimal(100))+" PLN";
    }

}
