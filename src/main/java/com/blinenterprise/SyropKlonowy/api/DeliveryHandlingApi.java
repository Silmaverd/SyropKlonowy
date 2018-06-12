package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.DeliveryStatus;
import com.blinenterprise.SyropKlonowy.view.DeliveryInProcess.DeliveryInProcessView;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.view.DeliveryView;
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
@RequestMapping("/api/deliveryHandling")
@Api
public class DeliveryHandlingApi {

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(path = "/deliveryHandling/beginDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Start handling a delivery", response = Response.class)
    private Response<DeliveryInProcessView> startDelivery(@RequestParam(value = "deliveryId") Long id){
        try{
            deliveryService.startHandlingADelivery(id);
            return new Response<DeliveryInProcessView>(true, Optional.empty());
        } catch (Exception e){
            log.error("Failed to start a delivery " + e.toString());
            return new Response<DeliveryInProcessView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/deliveryHandling/placeProduct", method = {RequestMethod.PUT})
    @ApiOperation(value = "Place product from a delivery", response = Response.class)
    private Response<DeliveryInProcessView> placeProduct(@RequestParam(value = "deliveryId") Long deliveryId,
                                  @RequestParam(value = "productId") Long productId,
                                  @RequestParam(value = "amount") int amount,
                                  @RequestParam(value = "sectorId") Long sectorId) {
        try{
            deliveryService.placeProduct(deliveryId, productId, amount, sectorId);
            return new Response<DeliveryInProcessView>(true, Optional.empty());
        } catch (Exception e){
            log.error("Failed to place given amount of product in given sector " + e.toString());
            return new Response<DeliveryInProcessView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/deliveryHandling/getProcessedDelivery", method = {RequestMethod.GET})
    @ApiOperation(value = "Get delivery in progres with ID", response = Response.class)
    private Response<DeliveryInProcessView> getDeliveryInProgressWithId(@RequestParam("deliveryId") Long id){
        try{
            Delivery deliveryInProgress = deliveryService.findById(id).get();
            DeliveryInProcessView deliveryInProcessView = DeliveryInProcessView.from(deliveryInProgress);
            return new Response<DeliveryInProcessView>(true, Arrays.asList(deliveryInProcessView));
        } catch (Exception e){
            log.error("Could not retrive delivery in progress with given id " + e.toString());
            return new Response<DeliveryInProcessView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/deliveryHandling/getAllDeliveriesWithStatus", method = {RequestMethod.GET})
    public Response<DeliveryView> getAllDeliveriesForWithStatus(@RequestParam(value = "status") String status){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAllWithStatus(DeliveryStatus.valueOf(status.toUpperCase())).stream().map(delivery ->
                    DeliveryView.from(delivery)
            ).collect(Collectors.toList()));
        }
        catch (Exception e){
            log.error("Failed to fetch deliveries "+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

}
