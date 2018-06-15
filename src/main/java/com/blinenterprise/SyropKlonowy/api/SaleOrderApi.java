package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.order.OrderClosureExecutor;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.SaleOrderView;
import com.blinenterprise.SyropKlonowy.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @RequestMapping(path = "addProductToOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a product to the client's basket", response = Response.class)
    public Response<SaleOrderView> addProductToClientOrder(
            @RequestParam(value = "clientId") Long clientId,
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "quantity") Integer quantity
    ) {
        try {
            saleOrderService.addProductToOrder(clientId, productId, quantity);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to add product. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "confirmClientOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Confirm and save the client's basket.", response = Response.class)
    public Response<SaleOrderView> confirmClientOrder(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            saleOrderService.confirmTempClientOrder(clientId);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to confirm order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "getOrderById", method = {RequestMethod.GET})
    @ApiOperation(value = "Display order by id", response = Response.class)
    public Response<SaleOrderView> getOrderById(@RequestParam(value = "orderId", required = true) Long orderId) {
        try {
            return new Response<SaleOrderView>(true, Arrays.asList(SaleOrderView.from(saleOrderService.findById(orderId))));
        } catch (Exception e) {
            log.error("Failed to retrieve order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "getTemporaryOrderByClientId", method = {RequestMethod.GET})
    @ApiOperation(value = "Display order that has not been yet confirmed by client id", response = Response.class)
    public Response<SaleOrderView> getTemporaryOrderByClientId(@RequestParam(value = "clientId", required = true) Long clientId) {
        try {
            return new Response<SaleOrderView>(true, Arrays.asList(SaleOrderView.from(
                    saleOrderService.findTemporaryOrderOfClient(clientId).orElse(null))));
        } catch (Exception e) {
            log.error("Failed to retrieve order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "removeProductFromTemporaryOrder", method = {RequestMethod.GET})
    @ApiOperation(value = "Remove product from order that has not been yet confirmed", response = Response.class)
    public Response<SaleOrderView> removeProductFromTemporaryOrder(@RequestParam(value = "clientId", required = true) Long clientId,
                                                                   @RequestParam(value = "productId", required = true) Long productId,
                                                                   @RequestParam(value = "quantity", required = true) Integer quantity) {
        try {
            saleOrderService.removeProductFromOrder(clientId, productId, quantity);
            return new Response<SaleOrderView>(true, Optional.empty());
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

    @RequestMapping(path = "getAllOrdersBySaleOrderStatus", method = {RequestMethod.GET})
    @ApiOperation(value = "Display orders", response = Response.class)
    public Response<SaleOrderView> getAllOrdersBySaleOrderStatus(
            @RequestParam(value = "status") String saleOrderStatus) {
        try {
            return new Response<SaleOrderView>(true, saleOrderService.findAllBySaleOrderStatus(
                    SaleOrderStatus.valueOf(saleOrderStatus)).stream().map(saleOrder ->
                    SaleOrderView.from(saleOrder)
            ).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Failed to retrieve orders. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "payOrderById", method = {RequestMethod.PUT})
    @ApiOperation(value = "Set the order with given ID as paid", response = Response.class)
    public Response<SaleOrderView> payOrderById(
            @RequestParam(value = "orderId") Long orderId
    ) {
        try {
            if (saleOrderService.payById(orderId)) {
                return new Response<SaleOrderView>(true, Optional.empty());
            } else {
                return new Response<SaleOrderView>(false, Optional.of("Failed to set order as paid. Wrong state."));
            }

        } catch (Exception e) {
            log.error("Failed to set order as paid. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "sendOrderById", method = {RequestMethod.PUT})
    @ApiOperation(value = "Set the order with given ID as sent", response = Response.class)
    public Response<SaleOrderView> sendOrderById(
            @RequestParam(value = "orderId") Long orderId) {
        try {
            if (saleOrderService.sendById(orderId)) {
                return new Response<SaleOrderView>(true, Optional.empty());
            } else {
                return new Response<SaleOrderView>(false, Optional.of("Failed to set order as sent. Wrong state."));
            }
        } catch (Exception e) {
            log.error("Failed to set order as sent. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }
}
