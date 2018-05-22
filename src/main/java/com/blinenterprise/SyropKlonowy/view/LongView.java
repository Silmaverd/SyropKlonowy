package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

import java.util.List;

@Getter
public class LongView implements View{
    private List<Long> data;

    private LongView(List<Long> data) {
        this.data = data;
    }

    public static LongView from(List<Long> list){
        return new LongView(list);
    }
}
