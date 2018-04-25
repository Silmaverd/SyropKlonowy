package com.blinenterprise.SyropKlonowy.order;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class OrderClosureExecutor {

    static private OrderClosureExecutor orderClosureExecutor;
    private List<OrderClosureCommand> commands;

    private OrderClosureExecutor(){
        commands = new LinkedList<>();
    }

    public void addClosureCommand(Long orderId, Date closureDate){
        commands.add(new OrderClosureCommand(closureDate, orderId));
    }

    static public OrderClosureExecutor getInstance() {
        if (orderClosureExecutor == null){
            orderClosureExecutor = new OrderClosureExecutor();
            return orderClosureExecutor;
        } else {
            return orderClosureExecutor;
        }
    }

    public void executeClosures(){
        Date now = new Date();
        LinkedList<OrderClosureCommand> commandsToBeClosed = new LinkedList<>();

        commands.forEach(command -> {
            if (command.dateEquals(now)) {
                System.out.println("CLOSING ORDER NR " + command.getOrderId());
                //TODO: close orders after implementing order model
                commandsToBeClosed.add(command);
            }
        });

        commandsToBeClosed.forEach(commandToBeRemoved -> {
            commands.remove(commandToBeRemoved);
        });
    }
}
