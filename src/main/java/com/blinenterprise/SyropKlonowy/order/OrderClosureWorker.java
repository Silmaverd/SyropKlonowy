package com.blinenterprise.SyropKlonowy.order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderClosureWorker implements Runnable{

    private volatile boolean isRunning = false;
    private volatile boolean shouldStop = false;
    private boolean orderClosureEnabled;
    private OrderClosureExecutor orderClosureExecutor;

    public OrderClosureWorker(OrderClosureExecutor orderClosureExecutor, boolean orderClosureEnabled) {
        this.orderClosureExecutor = orderClosureExecutor;
        this.orderClosureEnabled = orderClosureEnabled;
    }

    public void stop(){
        synchronized (this) {
            if (isRunning) {
                log.info("Stopping order closure worker...");
                shouldStop = true;
            } else {
                log.info("Process is not running");
            }
        }
    }

    @Override
    public void run() {
        isRunning = true;
        shouldStop = false;
        while (!shouldStop){
            try {
                if (orderClosureEnabled) orderClosureExecutor.executeClosures();
                Thread.sleep(60*60*1000); // 1 hour
            } catch (InterruptedException e) {
                isRunning = false;
                shouldStop = false;
                log.error("Automatic order closure process interrupted", e.getMessage());
            }
        }
    }
}
