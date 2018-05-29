package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.DataView;
import com.blinenterprise.SyropKlonowy.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController
@RequestMapping("/marketing")
@Api
public class MarketingApi {

    @Autowired
    SaleOrderService saleOrderService;

    @RequestMapping(path = "/client/showPriceRange", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders price range", response = Response.class)
    public Response<DataView> showPriceRange(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            DataView<String, String> marketingDataView = new DataView<>();
            marketingDataView.addToList("minimum price", MoneyConverter.getString(saleOrderService.findMinPriceInClientOrders(clientId)));
            marketingDataView.addToList("maximum price", MoneyConverter.getString(saleOrderService.findMaxPriceInClientOrders(clientId)));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show price range. Exception:" + e.getMessage());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/showMaxPrice", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders max price of product", response = Response.class)
    public Response<DataView> showMaxPrice(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            DataView<String, String> marketingDataView = new DataView<>();
            marketingDataView.addToList("maximum price", MoneyConverter.getString(saleOrderService.findMaxPriceOfProductInClientOrders(clientId)));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show price range. Exception:" + e.getMessage());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }


    @RequestMapping(path = "/client/showAveragePrice", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders average price of product", response = Response.class)
    public Response<DataView> showAveragePrice(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            DataView<String, String> marketingDataView = new DataView<>();
            marketingDataView.addToList("average price", MoneyConverter.getString(saleOrderService.findAveragePriceOfProductInClientOrders(clientId)));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show average price. Exception:" + e.getMessage());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/showMostCommonlyPurchasedProducts", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's most commonly purchased products", response = Response.class)
    public Response<DataView> showMostCommonlyPurchasedProducts(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            List<AmountOfProduct> listOfProductIdWithQuantity = saleOrderService.findMostCommonlyPurchasedProducts(clientId);
            DataView<Long, Integer> marketingDataView = new DataView<>(listOfProductIdWithQuantity
                    .stream().map(object -> new Pair<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show average price. Exception:" + e.getMessage());
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showFrequentlyBoughtTogether", method = {RequestMethod.GET})
    @ApiOperation(value = "show most frequently bought together products (id: frequency)", response = Response.class)
    public Response<DataView> showFrequentlyBoughtTogether(
            @RequestParam(value = "productId") Long productId
    ) {
        try {
            List<AmountOfProduct> listOfFrequentlyProduct = saleOrderService.findFrequentlyBoughtTogether(productId);
            DataView<Long, Integer> marketingDataView = new DataView<>(listOfFrequentlyProduct
                    .stream().map(object -> new Pair<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show frequently bought products. Exception:" + e.getMessage());
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

}
