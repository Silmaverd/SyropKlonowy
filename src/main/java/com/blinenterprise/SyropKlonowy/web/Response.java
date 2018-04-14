package com.blinenterprise.SyropKlonowy.web;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Response<T extends View> {
    private Boolean ok;
    private Optional<String> errorMessage;
    private T payload;

    public Response(Boolean ok, Optional<String> errorMessage) {
        this.ok = ok;
        this.errorMessage = errorMessage;
    }

    public Response(Boolean ok, T payload) {
        this.ok = ok;
        this.payload = payload;
    }
}
