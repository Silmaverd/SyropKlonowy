package com.blinenterprise.SyropKlonowy.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class Response<T extends View> {
    protected Boolean ok;
    protected Optional<String> errorMessage;
    protected List<T> payload;

    public Response(Boolean ok, Optional<String> errorMessage) {
        this.ok = ok;
        this.errorMessage = errorMessage;
    }

    public Response(Boolean ok, List<T> payload) {
        this.ok = ok;
        this.payload = payload;
    }

}
