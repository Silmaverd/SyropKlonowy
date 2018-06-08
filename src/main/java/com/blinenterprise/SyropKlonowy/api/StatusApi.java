package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.service.StatusService;
import com.blinenterprise.SyropKlonowy.view.ClientView;
import com.blinenterprise.SyropKlonowy.view.Response;
import com.blinenterprise.SyropKlonowy.view.status.PriceView;
import com.blinenterprise.SyropKlonowy.view.status.ProductsSoldView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @RequestMapping(path = "/status/getTotalIncomeFromOrdersSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows summed price of completed orders from specified date)", response = Response.class)
    public Response<PriceView> getTotalPriceOfOrdersSince(@RequestParam(value = "DD/MM/YYYY") String date) {
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
    public Response<ProductsSoldView> getNumberOfSoldProductsSince(@RequestParam(value = "DD/MM/YYYY") String date) {
        try {
            return new Response<>(true, Arrays.asList(
                    ProductsSoldView.from(statusService.getNumberOfProductsSoldSince(dateFormatter.parse(date)).intValue())
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve number of products sold");
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/status/getClientsActiveSince", method = {RequestMethod.GET})
    @ApiOperation(value = "shows clients that made orders since specified date)", response = Response.class)
    public Response<ClientView> getClientsActiveSince(@RequestParam(value = "DD/MM/YYYY") String date) {
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


}
