package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.SaleOrderedProduct;
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

import java.math.BigDecimal;
import java.util.*;
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

    @RequestMapping(path = "addOrder", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add an order", response = Response.class)
    public Response<SaleOrderView> addOrder(
            @RequestParam(value = "clientId") Long clientId,
            @ApiParam(value = "Date should be formatted as DD/MM/YYYY.")
            @RequestParam(value = "dateOfOrder") Date dateOfOrder,
            @ApiParam(value = "saleOrderedProducts should be provided pairs of productId and Quantity separated by a single space")
            @RequestParam(value = "saleOrderedProducts") String[] saleOrderedProducts,
            @RequestParam(value = "totalPrice") BigDecimal totalPrice
    ) {
        try {
            List<SaleOrderedProduct> saleOrderedProductsList = new ArrayList<SaleOrderedProduct>();
            log.error("saleOrderedProducts size is " + saleOrderedProducts.length);
            Arrays.asList(saleOrderedProducts).forEach(
                    prod -> saleOrderedProductsList.add(
                            new SaleOrderedProduct(
                                    Long.parseLong(prod.split("\\s")[0]),
                                    Integer.parseInt(prod.split("\\s")[1]))));
            SaleOrder saleOrder = new SaleOrder(clientId, dateOfOrder, saleOrderedProductsList, totalPrice, SaleOrderStatus.NEW);
            saleOrderService.create(saleOrder);
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to create order. Exception:" + e.toString());
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
    public Response<SaleOrderView> closeOrderById(@RequestParam(value = "orderId", required = true) Long orderId) {
        try {
            orderClosureExecutor.addClosureCommand(orderId, new Date());
            return new Response<SaleOrderView>(true, Optional.empty());
        } catch (Exception e) {
            log.error("Failed to close order. Exception:" + e.toString());
            return new Response<SaleOrderView>(false, Optional.of(e.toString()));
        }
    }


}
