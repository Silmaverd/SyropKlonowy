package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.blinenterprise.SyropKlonowy.view.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<List<WarehouseSectorProductsView>> sectorsWithProducts = new ArrayList<>();
        try {
            warehouseSectorService.findAll().stream()
                    .map(warehouseSector -> warehouseSector.getId())
                    .forEach(sectorId -> {
                        sectorsWithProducts.add(warehouseSectorService.findAllProductWithQuantitiesOnSector(sectorId).stream().map(productWithQuantity -> {
                            return WarehouseSectorProductsView.from(productWithQuantity.getProduct().getId(),
                                    productWithQuantity.getProduct().getName(),
                                    productWithQuantity.getProduct().getPrice(),
                                    productWithQuantity.getProduct().getCategory(),
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

    @RequestMapping(path = "/product/getAllNonReservedAmountsOfProducts", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all products with their unreserved amounts from all sectors", response = Response.class)
    public Response<ProductWithQuantityView> getAllProductsAndDistinctSectors() {
        List<ProductWithQuantityView> views = new ArrayList<>();
        try {
            productService.findAll()
                    .stream()
                    .forEach(product -> {
                        views.add(ProductWithQuantityView.from(
                                product.getId(),
                                product.getName(),
                                MoneyConverter.getString(product.getPrice()),
                                product.getCategory().toString(),
                                product.getProductionDate(),
                                product.getDescription(),
                                warehouseSectorService.findQuantityOfNotReservedProductOnAllSectorsByProductId(product.getId())
                        ));
                    });
            return new Response<ProductWithQuantityView>(true, views);
        } catch (Exception e) {
            return new Response<ProductWithQuantityView>(false, Optional.of(e.getMessage()));
        }

    }

    @RequestMapping(path = "/product/getByName", method = {RequestMethod.GET})
    @ApiOperation(value = "Display products by name", response = Response.class)
    public Response<ProductView> getProductByName(@RequestParam(value = "name", required = true) String name) {
        Response<ProductView> response;
        try {
            response = new Response<ProductView>(true, Arrays.asList(ProductView.from(productService.findByName(name).orElseThrow(IllegalArgumentException::new))));
        } catch (Exception e){
            response = new Response<ProductView>(false, Optional.of(e.toString()));
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
            response = new Response<ProductView>(false, Optional.of(e.toString()));
        }
        return response;

    }


}