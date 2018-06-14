package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class Response<T extends View> {
    private Boolean ok;
    private Optional<String> errorMessage;
    private List<T> payload;

    public Response(Boolean ok, Optional<String> errorMessage) {
        this.ok = ok;
        this.errorMessage = errorMessage;
    }

    public Response(Boolean ok, List<T> payload) {
        this.ok = ok;
        this.payload = payload;
    }

}
