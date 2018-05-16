package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.money.MoneyConverter;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.MarketingDataView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/marketing")
@Api
public class MarketingApi {

    @Autowired
    SaleOrderService saleOrderService;

    @RequestMapping(path = "/client/showPriceRange", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders price range", response = Response.class)
    public Response<MarketingDataView> showPriceRange(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            MarketingDataView marketingDataView = new MarketingDataView(new HashMap<>());
            marketingDataView.addToMap("minimum price", MoneyConverter.getString(saleOrderService.findMinPriceInClientOrders(clientId)));
            marketingDataView.addToMap("maximum price", MoneyConverter.getString(saleOrderService.findMaxPriceInClientOrders(clientId)));
            return new Response<MarketingDataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show price range. Exception:" + e.getMessage());
            return new Response<MarketingDataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/showMaxPrice", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders max price of product", response = Response.class)
    public Response<MarketingDataView> showMaxPrice(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            MarketingDataView marketingDataView = new MarketingDataView(new HashMap<>());
            marketingDataView.addToMap("maximum price", MoneyConverter.getString(saleOrderService.findMaxPriceOfProductInClientOrders(clientId)));
            return new Response<MarketingDataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show price range. Exception:" + e.getMessage());
            return new Response<MarketingDataView>(false, Optional.of(e.toString()));
        }
    }


    @RequestMapping(path = "/client/showAveragePrice", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's orders average price of product", response = Response.class)
    public Response<MarketingDataView> showAveragePrice(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            MarketingDataView marketingDataView = new MarketingDataView(new HashMap<>());
            marketingDataView.addToMap("average price", MoneyConverter.getString(saleOrderService.findAveragePriceOfProductInClientOrders(clientId)));
            return new Response<MarketingDataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show average price. Exception:" + e.getMessage());
            return new Response<MarketingDataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/showMostCommonlyPurchasedProducts", method = {RequestMethod.GET})
    @ApiOperation(value = "show client's most commonly purchased products", response = Response.class)
    public Response<MarketingDataView> showMostCommonlyPurchasedProducts(
            @RequestParam(value = "clientId") Long clientId
    ) {
        try {
            List<Object[]> listOfProductIdWithQuantity = saleOrderService.findMostCommonlyPurchasedProducts(clientId);
            MarketingDataView marketingDataView = new MarketingDataView(listOfProductIdWithQuantity
                    .stream().collect(Collectors.toMap(o -> ((Long)o[0]).toString(), o -> o[1])));
            return new Response<>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show average price. Exception:" + e.getMessage());
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showFrequentlyBoughtTogether", method = {RequestMethod.GET})
    @ApiOperation(value = "show most frequently bought together products (id: frequency)", response = Response.class)
    public Response<MarketingDataView> showFrequentlyBoughtTogether(
            @RequestParam(value = "productId") Long productId
    ) {
        try {
            List<Object[]> listOfFrequentlyProduct = saleOrderService.findFrequentlyBoughtTogether(productId);
            MarketingDataView marketingDataView = new MarketingDataView(listOfFrequentlyProduct
                    .stream().collect(Collectors.toMap(o -> ((Long)o[0]).toString(), o -> o[1])));
            return new Response<>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show frequently bought products. Exception:" + e.getMessage());
            return new Response<>(false, Optional.of(e.toString()));
        }
    }

}
