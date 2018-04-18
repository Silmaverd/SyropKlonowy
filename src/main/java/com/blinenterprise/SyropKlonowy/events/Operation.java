package com.blinenterprise.SyropKlonowy.events;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public abstract class Operation {
    protected String username;
    protected UUID processID;
    protected String description;

    public void persist(){
        log.info("Operation " + description + " invoked by " + username + " within process " + processID.toString());
    }
}
