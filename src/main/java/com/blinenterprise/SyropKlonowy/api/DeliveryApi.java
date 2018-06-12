package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.service.DeliveryService;
import com.blinenterprise.SyropKlonowy.view.DeliveryView;
import com.blinenterprise.SyropKlonowy.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
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
            @RequestParam(value = "price") String price,
            @RequestParam(value = "category") String category,
            @ApiParam(value = "Date in DD/MM/YYYY")
            @RequestParam(value = "productionDate") String date,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "quantity") int quantity,
            @RequestParam(value = "code") String code
    ){
        try{
            Product product = new Product(name, MoneyConverter.getBigDecimal(price), Category.valueOf(category.toUpperCase()), dateFormatter.parse(date), description, code);
            log.info(product.getName());
            deliveryService.addProductToDelivery(product, quantity);
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            log.error("Failed to add product to a delivery template "+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/delivery/confirmDelivery", method = {RequestMethod.PUT})
    @ApiOperation(value = "Save current delivery template as a delivery", response = Response.class)
    public Response<DeliveryView> performDelivery(){
        try{
            deliveryService.createDeliveryFromCurrentTemplate();
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            log.error("Failed to perform a delivery "+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/delivery/getDeliveryWithId", method = {RequestMethod.GET})
    public Response<DeliveryView> getDeliveryWithId(@RequestParam(value = "id", required = true) Long id){
        try {
            return new Response<DeliveryView>(true, Arrays.asList(DeliveryView.from(deliveryService.findById(id).orElseThrow(IllegalArgumentException::new))));
        }
        catch (Exception e){
            log.error("Failed to fetch deliveries "+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/delivery/getAllDeliveriesAfter", method = {RequestMethod.GET})
    public Response<DeliveryView> getAllDeliveriesAfter(@ApiParam(value = "Date in DD/MM/YYYY") @RequestParam(value = "date") String date){
        try {
            return new Response<DeliveryView>(true, deliveryService.findAllFrom(dateFormatter.parse(date)).stream().map(DeliveryView::from)
                    .collect(Collectors.toList()));
        }
        catch (Exception e){
            log.error("Failed to fetch deliveries "+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/delivery/getCurrentTemplate", method = {RequestMethod.GET})
    public Response<DeliveryView> getCurrentDeliveryTemplate(){
        try {
            return new Response<DeliveryView>(true, Arrays.asList(DeliveryView.from(deliveryService.getCurrentTempate())));
        }
        catch (Exception e){
            log.error("Failed to retrive current delivery template"+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/delivery/removeProductFromCurrentTemplate", method = {RequestMethod.GET})
    public Response<DeliveryView> removeProductFromCurrentTemplate(@RequestParam(value = "productName") String productName,
                                                                   @RequestParam(value = "quantity") Integer quantity){
        try {
            deliveryService.removeProductFromDelivery(productName, quantity);
            return new Response<DeliveryView>(true, Optional.empty());
        }
        catch (Exception e){
            log.error("Failed to retrive current delivery template"+e.toString());
            return new Response<DeliveryView>(false, Optional.of(e.getMessage()));
        }
    }
}
