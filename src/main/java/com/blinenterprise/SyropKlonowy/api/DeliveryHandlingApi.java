package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.view.DeliveryInProcess.DeliveryInProcessView;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController
@RequestMapping("/api/deliveryHandling")
@Api
public class DeliveryHandlingApi {

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(path = "/deliveryHandling/beginDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Start handling a delivery", response = Response.class)
    private Response startDelivery(@RequestParam(value = "delivery id") Long id){
        try{
            deliveryService.startHandlingADelivery(id);
            return new Response(true, Optional.empty());
        } catch (Exception e){
            log.error("Failed to start a delivery " + e.toString());
            return new Response(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/deliveryHandling/placeProduct", method = {RequestMethod.PUT})
    @ApiOperation(value = "Place product from a delivery", response = Response.class)
    private Response placeProduct(@RequestParam(value = "delivery id") Long deliveryId,
                                  @RequestParam(value = "product id") Long productId,
                                  @RequestParam(value = "amount placed") int amount,
                                  @RequestParam(value = "sector id") Long sectorId) {
        try{
            deliveryService.placeProduct(deliveryId, productId, amount, sectorId);
            return new Response(true, Optional.empty());
        } catch (Exception e){
            log.error("Failed to place given amount of product in given sector " + e.toString());
            return new Response(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/deliveryHandling/getProcessedDelivery", method = {RequestMethod.GET})
    @ApiOperation(value = "Get delivery in progres with ID", response = Response.class)
    private Response getDeliveryInProgressWithId(@RequestParam("delivery id") Long id){
        try{
            Delivery deliveryInProgress = deliveryService.findById(id).get();
            DeliveryInProcessView deliveryInProcessView = DeliveryInProcessView.from(deliveryInProgress);
            return new Response(true, Arrays.asList(deliveryInProcessView));
        } catch (Exception e){
            log.error("Could not retrive delivery in progress with given id " + e.toString());
            return new Response(false, Optional.of(e.toString()));
        }
    }

}
