package com.blinenterprise.SyropKlonowy.web;

import java.util.List;

public class ListResponse<T extends View> extends Response<T>{
    private List<T> payload;


    public ListResponse(Boolean ok, List<T> payload) {
        this.ok = ok;
        this.payload = payload;
    }
}
