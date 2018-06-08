package com.blinenterprise.SyropKlonowy.converter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyConverterTest {

    @Test
    public void test(){
        BigDecimal priceDot = MoneyConverter.getBigDecimal("10.40");
        BigDecimal pricePrzec = MoneyConverter.getBigDecimal("10,40");
        BigDecimal priceNo = MoneyConverter.getBigDecimal("10");

        Assert.assertEquals(1040L, priceDot.longValue());
        Assert.assertEquals(1040L, pricePrzec.longValue());
        Assert.assertEquals(1000L, priceNo.longValue());
    }
}
