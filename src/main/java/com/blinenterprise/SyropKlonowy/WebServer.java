package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.config.ConfigContainer;
import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.order.OrderClosureWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
public class WebServer implements CommandLineRunner {

    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private ConfigContainer configContainer;
    @Autowired
    private OrderClosureExecutor orderClosureExecutor;

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        dataLoader.loadTestDataBase();

        ExecutorService executor = Executors.newCachedThreadPool();


        OrderClosureWorker orderClosureWorker = new OrderClosureWorker(orderClosureExecutor,
                configContainer.getOrderAutoClosure());

        executor.submit(orderClosureWorker);
    }
}