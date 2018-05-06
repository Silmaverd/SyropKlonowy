package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/delivery")
@Api
public class DeliveryApi {

    @Autowired
    private DeliveryService deliveryService;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    @RequestMapping(path = "/delivery/addProductToTemplate", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a product to currently prepared delivery", response = Response.class)
    public Response<DeliveryView> addProductToDeliveryTemplate (
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "category") String category,
            @RequestParam(value = "date in DD/MM/YYYY") String date,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "quantity") int quantity,
            @RequestParam(value = "code") String code
    ){
        try{
            Product product = new Product(name, price, Category.valueOf(category.toUpperCase()), dateFormatter.parse(date), description, code);
            deliveryService.addProductToDelivery(product, quantity);
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/delivery/performDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Save current delivery template as a delivery", response = Response.class)
    public Response<DeliveryView> performDelivery(
            @RequestParam(value = "warehouse name") String warehouseName
    ){
        try{
            deliveryService.performDeliveryFromCurrentTemplate(warehouseName);
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getDelivery", method = {RequestMethod.GET})
    public Response<DeliveryView> getDelivery(@RequestParam(value = "id", required = true) Long id){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAllById(id).stream().map(delivery ->
                    DeliveryView.from(delivery)
            ).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getAllDeliveries", method = {RequestMethod.GET})
    public Response<DeliveryView> getAllDeliveries(){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAll().stream().map( delivery ->
                    DeliveryView.from(delivery)
            ).collect(Collectors.toList()));
        }
        catch (Exception e){
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }
}
