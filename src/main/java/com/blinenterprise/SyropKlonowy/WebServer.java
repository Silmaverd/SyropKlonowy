package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.config.ConfigContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WebServer implements CommandLineRunner {

    @Autowired
    private ConfigContainer configContainer;

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(configContainer.getOrderAutoClosure().toString());
    }
}
