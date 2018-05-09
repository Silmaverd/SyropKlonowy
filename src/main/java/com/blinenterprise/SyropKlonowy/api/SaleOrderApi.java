package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.SaleOrderView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/sale_order")
@Api
public class SaleOrderApi {

    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    OrderClosureExecutor orderClosureExecutor;

    @RequestMapping(path = "startOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Start an order template for a client.", response = Response.class)
    public Response<SaleOrderView> createOrder(
            @RequestParam(value = "clientId") Long clientId,
            @ApiParam(value = "Date should be formatted as DD/MM/YYYY.")
            @RequestParam(value = "dateOfOrder") Date dateOfOrder
    ) {
        try {
            saleOrderService.startOrder(clientId, dateOfOrder);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to create order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "addProductToOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a product to the current order", response = Response.class)
    public Response<SaleOrderView> createOrder(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "quantity") Integer quantity
    ) {
        try {
            saleOrderService.addProductToCurrentOrder(productId, quantity);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to add product. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "confirmOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Confirm and save the current order.", response = Response.class)
    public Response<SaleOrderView> confirmOrder() {
        try {
            saleOrderService.confirmCurrentOrder();
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to confirm order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "getOrderById", method = {RequestMethod.GET})
    @ApiOperation(value = "Display order by id", response = Response.class)
    public Response<SaleOrderView> getOrderById(@RequestParam(value = "orderId") Long orderId) {
        try {
            return new Response<SaleOrderView>(true, Arrays.asList(SaleOrderView.from(saleOrderService.findById(orderId))));
        } catch (Exception e) {
            log.error("Failed to retrieve order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "getAllOrders", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all orders", response = Response.class)
    public Response<SaleOrderView> getAllOrders() {
        try {
            return new Response<SaleOrderView>(true, saleOrderService.findAll().stream().map(saleOrder ->
                    SaleOrderView.from(saleOrder)
            ).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Failed to retrieve orders. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "closeOrderById", method = {RequestMethod.PUT})
    @ApiOperation(value = "Close an order by id", response = Response.class)
    public Response<SaleOrderView> closeOrderById(@RequestParam(value = "orderId") Long orderId) {
        try {
            orderClosureExecutor.addClosureCommand(orderId, new Date());
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to close order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "sendOrderByIdToWarehouse", method = {RequestMethod.PUT})
    @ApiOperation(value = "Send an order of a given ID to the named warehouse", response = Response.class)
    public Response<SaleOrderView> sendOrderByIdToWarehouse(
            @RequestParam(value = "orderId") Long orderId,
            @RequestParam(value = "warehouseName") String warehouseName) {
        try {
            saleOrderService.sendOrderByIdToWarehouse(orderId, warehouseName);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to send order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

}
