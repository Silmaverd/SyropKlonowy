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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery")
@Api
public class DeliveryApi {

    @Autowired
    DeliveryService deliveryService;


    @RequestMapping(path = "/delivery/addDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a delivery", response = Response.class)
    public Response<DeliveryView> addDelivery(@RequestParam(value = "date in dd/MM/yyyy", required = true)String date){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            deliveryService.create(new Delivery(formatter.parse(date)));
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getDelivery", method = {RequestMethod.GET})
    public Response<DeliveryView> getDelivery(@RequestParam(value = "id", required = true) Long id){
        try {
            List<Delivery> listOfDelivery = deliveryService.findAllById(id);
            List<DeliveryView> returnList = new ArrayList<>();

            for (Delivery d : listOfDelivery) {
                returnList.add(DeliveryView.from(d));
            }

            return new Response<DeliveryView>(true, returnList);
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

}
