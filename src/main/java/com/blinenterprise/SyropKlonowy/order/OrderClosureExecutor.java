package com.blinenterprise.SyropKlonowy.order;

import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@Scope(value = "singleton")
public class OrderClosureExecutor {

    private List<OrderClosureCommand> commands;

    @Autowired
    SaleOrderService saleOrderService;

    private OrderClosureExecutor(){
        commands = new LinkedList<>();
    }

    public void addClosureCommand(Long orderId, Date closureDate){
        commands.add(new OrderClosureCommand(closureDate, orderId));
    }



    public void executeClosures(){
        Date now = new Date();
        LinkedList<OrderClosureCommand> commandsToBeClosed = new LinkedList<>();

        commands.forEach(command -> {
            if (command.dateEquals(now)) {
                try {
                    saleOrderService.closeById(command.getOrderId());
                } catch (Exception e) {
                    log.error("Could not close order, exception: " + e.toString());
                }
                commandsToBeClosed.add(command);
            }
        });

        commandsToBeClosed.forEach(commandToBeRemoved -> {
            commands.remove(commandToBeRemoved);
        });
    }
}
