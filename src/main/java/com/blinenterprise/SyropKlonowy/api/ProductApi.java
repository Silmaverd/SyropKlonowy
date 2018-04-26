package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.view.ProductView;
import com.blinenterprise.SyropKlonowy.web.Response;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Api(value = "Product Viewing API")
class ProductApi {
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/product/getAll", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products by name", response = Response.class)
    public Response<ProductView> getAllProducts() {
        Response<ProductView> response;
        try {
            ArrayList<Product> result = Lists.newArrayList(productService.findAll());
            response = new Response<ProductView>(true, ProductView.from(result));
        } catch (Exception e) {
            response = new Response<ProductView>(false, Optional.of(e.getMessage()));
        }
        return response;

    }

    @RequestMapping(path = "/product/getByName", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products by name", response = Response.class)
    public Response<ProductView> getProductByName(@RequestParam(value = "name", required = true) String name) {
        Response<ProductView> response;
        try {
            ArrayList<Product> result = Lists.newArrayList(productService.findAllByName(name));
            response = new Response<ProductView>(true, ProductView.from(result));
        } catch (Exception e){
            response = new Response<ProductView>(false, Optional.of(e.getMessage()));
        }
        return response;

    }

    @RequestMapping(path = "/product/getById", method = {RequestMethod.GET})
    @ApiOperation(value = "Display product by id", response = Response.class)
    public Response<ProductView> getProductByName(@RequestParam(value = "id", required = true) Long id) {
        Response<ProductView> response;
        try {
            Optional<Product> result = productService.findById(id);
            response = new Response<ProductView>(true, Lists.newArrayList(ProductView.from(result.get())));
        } catch (Exception e) {
            response = new Response<ProductView>(false, Optional.of(e.getMessage()));
        }
        return response;

    }



}