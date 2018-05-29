package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.order.OrderClosureWorker;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
public class WebServer implements CommandLineRunner {

    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private OrderClosureExecutor orderClosureExecutor;
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        dataLoader.loadTestDataBase();

        ExecutorService executor = Executors.newCachedThreadPool();


        OrderClosureWorker orderClosureWorker = new OrderClosureWorker(orderClosureExecutor, Boolean.valueOf(environment.getProperty("orderAutoClosure")));

        executor.submit(orderClosureWorker);
    }
}