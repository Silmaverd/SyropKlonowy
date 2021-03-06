package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.service.SaleOrderReportService;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.DataView;
import com.blinenterprise.SyropKlonowy.view.DataViewValue;
import com.blinenterprise.SyropKlonowy.view.Response;
import com.blinenterprise.SyropKlonowy.view.SaleReportView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/marketing")
@Api
public class MarketingApi {

    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    SaleOrderReportService saleOrderReportService;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


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
            log.error("Failed to show price range. Exception:" + e.toString());
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
            log.error("Failed to show price range. Exception:" + e.toString());
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
            log.error("Failed to show average price. Exception:" + e.toString());
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
                    .stream().map(object -> new DataViewValue<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show average price. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
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
                    .stream().map(object -> new DataViewValue<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show frequently bought products. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }


    @RequestMapping(path = "/product/showMostFrequentlyBoughtThisWeek", method = {RequestMethod.GET})
    @ApiOperation(value = "show most frequently bought in last week", response = Response.class)
    public Response<DataView> showMostFrequentlyBoughtThisWeek(
    ) {
        try {
            List<AmountOfProduct> listOfFrequentlyProduct = saleOrderService.findFrequentlyBoughtInLastWeek();
            DataView<Long, Integer> marketingDataView = new DataView<>(listOfFrequentlyProduct
                    .stream().map(object -> new DataViewValue<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));


        } catch (Exception e) {
            log.error("Failed to show frequently bought products. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showMostFrequentlyBoughtThisWeekByEnterprise", method = {RequestMethod.GET})
    @ApiOperation(value = "show most frequently bought in last week by Enterprise", response = Response.class)
    public Response<DataView> showMostFrequentlyBoughtThisWeekByEnterprise(
            @RequestParam(value = "enterpriseType") Enterprise enterpriseType
    ) {
        try {
            List<AmountOfProduct> listOfFrequentlyProduct = saleOrderService.findFrequentlyBoughtInLastWeek(enterpriseType);
            DataView<Long, Integer> marketingDataView = new DataView<>(listOfFrequentlyProduct
                    .stream().map(object -> new DataViewValue<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));


        } catch (Exception e) {
            log.error("Failed to show frequently bought products. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showBoughtProductsSum", method = {RequestMethod.GET})
    @ApiOperation(value = "show bought products sum", response = Response.class)
    public Response<DataView> showBoughtProductsSum(
    ) {
        try {
            List<AmountOfProduct> listOfBoughtProductsSum = saleOrderService.findBoughtProductsSum();
            DataView<Long, Integer> marketingDataView = new DataView<>(listOfBoughtProductsSum
                    .stream().map(object -> new DataViewValue<>(object.getProductId(), object.getQuantity())).collect(Collectors.toList()));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show bought products sum. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showIncomeFromOrders", method = {RequestMethod.GET})
    @ApiOperation(value = "show income from orders", response = Response.class)
    public Response<DataView> findIncomeFromOrders(
            @RequestParam(value = "dateFrom in 'YYYY-MM-DD'") String dateFrom,
            @RequestParam(value = "toDate in 'YYYY-MM-DD'") String toDate
    ) {
        try {
            BigDecimal incomeFromOrders = saleOrderService.findIncomeFromOrders(dateFrom, toDate);
            DataView<String, String> marketingDataView = new DataView<>(Arrays.asList(new DataViewValue<String,String>("gain", incomeFromOrders.toString())));
            return new Response<DataView>(true, Lists.newArrayList(marketingDataView));

        } catch (Exception e) {
            log.error("Failed to show income from orders. Exception:" + e.toString());
            return new Response<DataView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/product/showSalesReportForPeriod", method = {RequestMethod.GET})
    @ApiOperation(value = "show a sales report for the period of time provided", response = Response.class)
    public Response<SaleReportView> showSalesReportForPeriod(
            @ApiParam(value = "Date in DD/MM/YYYY")
            @RequestParam(value = "startDate") String startDate,
            @ApiParam(value = "Date in DD/MM/YYYY")
            @RequestParam(value = "endDate") String endDate
    ) {
        try {
            SaleReportView saleReportView = saleOrderReportService.generateWithinPeriod(
                    dateFormatter.parse(startDate), dateFormatter.parse(endDate));
            return new Response<>(true, Lists.newArrayList(saleReportView));
        } catch (Exception e) {
            log.error("Failed to create a sale order report. Exception:" + e.toString());
            return new Response<>(true, Optional.of(e.toString()));
        }
    }


}
