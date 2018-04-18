package com.blinenterprise.SyropKlonowy.events;

import java.util.UUID;

public class SystemOperation extends Operation{

    public SystemOperation(String description, UUID processID){
        this.description = description;
        this.processID = processID;
        this.username = "system";
    }
}
