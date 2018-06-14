package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.blinenterprise.SyropKlonowy.view.ProductView;
import com.blinenterprise.SyropKlonowy.view.Response;
import com.blinenterprise.SyropKlonowy.view.WarehouseSectorProductsView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@Api(value = "Product Viewing API")
class ProductApi {
    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseSectorService warehouseSectorService;

    @RequestMapping(path = "/product/getAll", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all products", response = Response.class)
    public Response<WarehouseSectorProductsView> getAllProducts() {
        Response<WarehouseSectorProductsView> response;
        List<List<WarehouseSectorProductsView>> sectorsWithProducts = new ArrayList<>();
        try {
            warehouseSectorService.findAll().stream()
                    .map(warehouseSector -> warehouseSector.getId())
                    .forEach(sectorId -> {
                        sectorsWithProducts.add(warehouseSectorService.findAllProductWithQuantitiesOnSector(sectorId).stream().map(productWithQuantity -> {
                            return WarehouseSectorProductsView.from(productWithQuantity.getProduct().getName(),
                                    productWithQuantity.getProduct().getPrice(),
                                    productWithQuantity.getProduct().getDescription(),
                                    sectorId,
                                    productWithQuantity.getQuantity());
                        }).collect(Collectors.toList()));
                    });
            List<WarehouseSectorProductsView> squashedSectorsWithProducts = new ArrayList<>();
            sectorsWithProducts.forEach(sectorWithProducts -> sectorWithProducts.forEach(view -> squashedSectorsWithProducts.add(view)));
            return new Response<WarehouseSectorProductsView>(true, squashedSectorsWithProducts);
        } catch (Exception e) {
            return new Response<WarehouseSectorProductsView>(false, Optional.of(e.toString()));
        }

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
    public Response<ProductView> getProductById(@RequestParam(value = "id", required = true) Long id) {
        Response<ProductView> response;
        try {
            ArrayList<Product> result = new ArrayList<>();
            productService.findById(id).ifPresent(result::add);
            response = new Response<ProductView>(true, ProductView.from(result));
        } catch (Exception e) {
            response = new Response<ProductView>(false, Optional.of(e.getMessage()));
        }
        return response;

    }



}