package com.blinenterprise.SyropKlonowy.domain.money;


public class Money {

    private Long amount;

    public Money add(Money money){ return new Money(this.amount + money.amount); }

    public Money subtract(Money money){ return new Money(this.amount - money.amount); }

    public Money multiply(int multiple){ return new Money(amount * multiple); }

    public Money divide(int divider){ return new Money(amount / divider); }


    public String getString(){ return String.valueOf(amount /100 + "," + amount %100); }

    public float getFloat(){ return (float)amount/100; }


    static Money valueOf(String amount){ return new Money(amount); }

    static Money valueOf(Long amount){ return new Money(amount); }


    private Money(Long amount) {
        this.amount = amount;
    }

    private Money(String amount) {
        float amountFloat = Float.parseFloat(amount);
        this.amount = (long)(amountFloat*100);
    }
}
