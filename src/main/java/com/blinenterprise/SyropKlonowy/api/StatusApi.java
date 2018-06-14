package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.service.StatusService;
import com.blinenterprise.SyropKlonowy.view.ClientView;
import com.blinenterprise.SyropKlonowy.view.Response;
import com.blinenterprise.SyropKlonowy.view.status.PriceView;
import com.blinenterprise.SyropKlonowy.view.status.NumberView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/status")
@Api
public class StatusApi {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private StatusService statusService;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(path = "/status/getTotalIncomeFromOrdersSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed price of completed orders from specified date)", response = Response.class)
    public Response<PriceView> getTotalPriceOfOrdersSince(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date) {
        try {
            return new Response<>(true, Arrays.asList(
                    PriceView.from(statusService.getTotalIncomeFromOrdersSince(dateFormatter.parse(date)))
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve total income");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getNumberOfProductsSoldSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed number of sold products from specified date)", response = Response.class)
    public Response<NumberView> getNumberOfSoldProductsSince(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date) {
        try {
            return new Response<>(true, Arrays.asList(
                    NumberView.from(statusService.getNumberOfProductsSoldSince(dateFormatter.parse(date)).intValue())
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve number of products sold");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getClientsActiveSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows clients that made orders since specified date)", response = Response.class)
    public Response<ClientView> getClientsActiveSince(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date) {
        try {
            return new Response<>(true, statusService.getClientsActiveSice(dateFormatter.parse(date))
                    .stream()
                    .map(client -> ClientView.from(client))
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.error("Failed to retrieve active clients");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getAmountOfSectors", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed number of sectors)", response = Response.class)
    public Response<NumberView> getNumberOfSoldProductsSince() {
        try {
            return new Response<>(true, Arrays.asList(
                    NumberView.from(statusService.getAmountOfWarehouseSectors())
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve number of sectors");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getNumbersOfOrdersMadeSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed number of orders made since specified date)", response = Response.class)
    public Response<NumberView> getNumbersOfOrdersMadeSince(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date) {
        try {
            return new Response<>(true, Arrays.asList(
                    NumberView.from(saleOrderService.findAllSaleOrdersSince(dateFormatter.parse(date)).size())
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve number of orders");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getNumberOfHandledDeliveriesSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed number of deliveries handled since specified date)", response = Response.class)
    public Response<NumberView> getNumberOfHandledDeliveriesSince(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date) {
        try {
            return new Response<>(true, Arrays.asList(
                    NumberView.from(deliveryService.findAllFrom(dateFormatter.parse(date))
                    .stream()
                    .filter(delivery -> delivery.getDeliveryStatus().equals(DeliveryStatus.DONE))
                    .mapToInt(delivery -> 1)
                    .sum())
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve number of deliveries");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }
}
