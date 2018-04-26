package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.view.ProductView;
import com.blinenterprise.SyropKlonowy.web.ListResponse;
import com.blinenterprise.SyropKlonowy.web.Response;
import com.blinenterprise.SyropKlonowy.web.SingleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/productview")
@Api(value = "Product Viewing API")
class ProductViewApi{
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/productview/id", method = {RequestMethod.GET})
    @ApiOperation(value = "Display product id", response = Response.class)
    public Response<ProductView> getProductById(@RequestParam(value = "id", required = true) Long id) {
        Response<ProductView> response;
        try {
            Product result = productService.findById(id);
            response = new SingleResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new SingleResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/name", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products by name", response = Response.class)
    public Response<ProductView> getProductByName(@RequestParam(value = "name", required = true) String name) {
        Response<ProductView> response;
        try {
            ArrayList<Product> result = productService.findAllByName(name);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/minprice", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products of at least given price", response = Response.class)
    public Response<ProductView> getProductsByMinPrice(@RequestParam(value = "price", required = true) Double price) {
        Response<ProductView> response;
        try {
            List<Product> result = productService.findByMinPrice(price);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/maxprice", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products of at most given price", response = Response.class)
    public Response<ProductView> getProductsByMaxPrice(@RequestParam(value = "price", required = true) Double price) {
        Response<ProductView> response;
        try {
            List<Product> result = productService.findByMaxPrice(price);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/category", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products of given category", response = Response.class)
    public Response<ProductView> getProductsByCategory(@RequestParam(value = "category", required = true) Category category) {
        Response<ProductView> response;
        try {
            List<Product> result = productService.findAllOfCategory(category);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/fromdate", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products added after a date", response = Response.class)
    public Response<ProductView> getProductsFromDate(@RequestParam(value = "productionDate", required = true) Date date) {
        Response<ProductView> response;
        try {
            List<Product> result = productService.findAllAfterDate(date);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }

    @RequestMapping(path = "/productview/todate", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products added before a date", response = Response.class)
    public Response<ProductView> getProductsAfterDate(@RequestParam(value = "productionDate", required = true) Date date) {
        Response<ProductView> response;
        try {
            List<Product> result = productService.findAllBeforeDate(date);
            response = new ListResponse<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new ListResponse<ProductView>(false, java.util.Optional.ofNullable(e.getMessage()));
        }
        return response;
    }
}