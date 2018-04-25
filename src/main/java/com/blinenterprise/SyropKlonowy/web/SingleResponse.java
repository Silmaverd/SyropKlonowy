package com.blinenterprise.SyropKlonowy.web;

import java.util.Optional;

public class SingleResponse<T extends View> extends Response{
    public SingleResponse(Boolean ok, Optional<String> errorMessage) {
        super(ok, errorMessage);
    }

    public SingleResponse(Boolean ok, T payload) {
        super(ok, payload);
    }
}
