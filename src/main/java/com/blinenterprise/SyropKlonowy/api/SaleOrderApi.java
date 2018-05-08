package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.SaleOrderView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale_order")
@Api
public class SaleOrderApi {

    @Autowired
    SaleOrderService saleOrderService;

    @RequestMapping(path = "addOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add an order", response = Response.class)
    public Response<SaleOrderView> addOrder() {
        try {
            SaleOrder saleOrder = new SaleOrder();
            saleOrderService.create(saleOrder);
            return new Response<SaleOrderView>(true, Optional.of("id of created order:" + saleOrder.getId()));
        } catch (Exception e) {
            return new Response<SaleOrderView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "getOrder", method = {RequestMethod.GET})
    @ApiOperation(value = "Display order by id", response = Response.class)
    public Response<SaleOrderView> getOrder(@RequestParam(value = "id", required = true) Long id) {
        try {
            return new Response<SaleOrderView>(true, Arrays.asList(SaleOrderView.from(saleOrderService.findById(id))));
        } catch (Exception e) {
            return new Response<SaleOrderView>(false, Optional.of(e.getMessage()));
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
            return new Response<SaleOrderView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "closeOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Close an order by id", response = Response.class)
    public Response<SaleOrderView> closeOrder(@RequestParam(value = "id", required = true) Long id) {
        //Run closure command here, return the order view after it is closed.
        return new Response<SaleOrderView>(false, Optional.of("nah"));
    }


}
