package com.blinenterprise.SyropKlonowy.events;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class Operation {
    public String username;
    public UUID processID;
    public String description;

    public void persist(){
        log.info("Operation " + description + " invoked by " + username + " within process " + processID.toString());
    }
}
