package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Delivery;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.view.DeliveryView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/delivery")
@Api
public class DeliveryApi {

    @Autowired
    DeliveryService deliveryService;


    @RequestMapping(path = "/delivery/addDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a delivery", response = Response.class)
    public Response<DeliveryView> addDelivery(){
        try{
            Delivery delivery = new Delivery();
            deliveryService.createDelivery(delivery);
            return new Response<DeliveryView>(true, Optional.of("id of delivery: "+delivery.getId()));
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getDelivery", method = {RequestMethod.GET})
    public Response<DeliveryView> getDelivery(@RequestParam(value = "id", required = true) Long id){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAllById(id).stream().map( delivery -> DeliveryView.from(delivery) ).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getAllDelivery", method = {RequestMethod.GET})
    public Response<DeliveryView> getAllDelivery(){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAll().stream().map( delivery -> DeliveryView.from(delivery) ).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

}
