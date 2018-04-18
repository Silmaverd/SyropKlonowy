package com.blinenterprise.SyropKlonowy.events;

import java.util.UUID;

public class UserOperation extends Operation{

    public UserOperation(String description, String username, UUID processID){
        this.description = description;
        this.processID = processID;
        this.username = username;
    }
}

