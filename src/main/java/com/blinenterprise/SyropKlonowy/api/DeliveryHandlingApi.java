package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Delivery.processing.DeliveryProcessed;
import com.blinenterprise.SyropKlonowy.domain.Delivery.processing.DeliveryProcessedView;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
            DeliveryProcessed deliveryProcessed = deliveryService.findDeliveryInProgressForId(id).get();
            return new Response(true, Arrays.asList(
                    DeliveryProcessedView.from(deliveryProcessed.getListOfProductsToDeliver(), deliveryProcessed.getDeliveryId()))
            );
        } catch (Exception e){
            log.error("Could not retrive delivery in progress with given id " + e.toString());
            return new Response(false, Optional.of(e.toString()));
        }
    }

}
