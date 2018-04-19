package com.blinenterprise.SyropKlonowy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
public class ConfigContainer {

    @Value("${sk.orderAutoClosure.enabled}")
    private Boolean orderAutoClosure;
}
