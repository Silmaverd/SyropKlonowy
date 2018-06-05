package com.blinenterprise.SyropKlonowy.converter;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class AmountOfProductConverter {

    public static List<AmountOfProduct> getAmountOfProductListFromBigIntegerAndBigDecimal(List<Object[]> listOfObjects){
        return listOfObjects.stream().map(object -> new AmountOfProduct(((BigInteger) object[0]).longValue(), ((BigDecimal) object[1]).intValue())).collect(Collectors.toList());
    }
    public static List<AmountOfProduct> getAmountOfProductListFromLongAndLong(List<Object[]> listOfObjects){
        return listOfObjects.stream().map(object -> new AmountOfProduct((Long) object[0], (int)(long) object[1])).collect(Collectors.toList());
    }

    public static List<AmountOfProduct> getAmountOfProductListFromBigIntegerAndBigInteger(List<Object[]> listOfObjects){
        return listOfObjects.stream().map(object -> new AmountOfProduct(((BigInteger) object[0]).longValue(), ((BigInteger) object[1]).intValue())).collect(Collectors.toList());
    }
}
