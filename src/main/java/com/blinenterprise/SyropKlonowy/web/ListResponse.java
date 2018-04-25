package com.blinenterprise.SyropKlonowy.web;

import java.util.List;
import java.util.Optional;

public class ListResponse<T extends View> extends Response<T>{
    private List<T> payload;

    public ListResponse(Boolean ok, Optional<String> errorMessage) {
        super(ok, errorMessage);
    }

    public ListResponse(Boolean ok, List<T> payload) {
        this.ok = ok;
        this.payload = payload;
    }
}
