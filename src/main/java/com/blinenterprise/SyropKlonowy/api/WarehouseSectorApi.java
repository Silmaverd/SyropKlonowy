package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.blinenterprise.SyropKlonowy.view.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/warehouseSector")
@Api
public class WarehouseSectorApi {

    @Autowired
    private WarehouseSectorService warehouseSectorService;

    @RequestMapping(path = "/warehouseSector/getById", method = {RequestMethod.GET})
    @ApiOperation(value = "Display warehouseSector by id", response = Response.class)
    public Response<WarehouseSectorView> getWarehouseSectorById(@RequestParam(value = "id", required = true) Long id) {
        try {
            return new Response<WarehouseSectorView>(true, Arrays.asList(WarehouseSectorView.from(warehouseSectorService.findById(id).orElseThrow(IllegalArgumentException::new))));
        } catch (Exception e) {
            log.error("Failed to fetch warehouseSectors "+e.toString());
            return new Response<WarehouseSectorView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/warehouseSector/addWarehouseSector", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a warehouseSector", response = Response.class)
    public Response<WarehouseSectorView> addWarehouseSector(@RequestParam(value = "name", required = true) String name,
                                                            @RequestParam(value = "maxAmountOfProducts") Integer maxAmountOfProducts) {
        try {
            WarehouseSector warehouseSector = new WarehouseSector(name, maxAmountOfProducts);
            warehouseSectorService.saveOrUpdate(warehouseSector);
            return new Response<WarehouseSectorView>(true, Optional.empty());
        } catch (Exception e) {
            return new Response<WarehouseSectorView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/warehouseSector/getAll", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all warehouseSectors", response = Response.class)
    public Response<WarehouseSectorView> getAllWarehouseSectors() {
        Response<WarehouseSectorView> response;
        try {
            List<WarehouseSector> result = warehouseSectorService.findAll();
            response = new Response<WarehouseSectorView>(true, WarehouseSectorView.from(result));
        } catch (Exception e) {
            response = new Response<WarehouseSectorView>(false, Optional.of(e.getMessage()));
        }
        return response;

    }

    @RequestMapping(path = "/warehouseSector/getByName", method = {RequestMethod.GET})
    @ApiOperation(value = "Display warehouseSectors by name", response = Response.class)
    public Response<WarehouseSectorView> getWarehouseSectorByName(@RequestParam(value = "name", required = true) String name) {
        try {
            return new Response<WarehouseSectorView>(true, Arrays.asList(WarehouseSectorView.from(warehouseSectorService.findByName(name).orElseThrow(IllegalArgumentException::new))));
        } catch (Exception e) {
            return new Response<WarehouseSectorView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/warehouseSector/getAllContainingNotReservedProduct", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all warehouse ids that contain product on not reserved list", response = Response.class)
    public Response<ProductInSectorView> getWarehouseIdsConatiningProduct(@RequestParam(value = "productId", required = true) Long productId) {
        try {
            List<ProductInSectorView> productInSectors = warehouseSectorService
                    .findAllContainingNotReservedProductOrderedASCByProductId(productId)
                    .stream()
                    .map(sector -> ProductInSectorView.from(
                            sector.getId(),
                            productId,
                            warehouseSectorService.findQuantityOfNotReservedProductOnSectorByProductId(sector.getId(), productId)
                    ))
                    .collect(Collectors.toList());

            return new Response<>(true, productInSectors);
        } catch (Exception e) {
            return new Response<ProductInSectorView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/warehouseSector/getAllContainingReservedProduct", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all warehouse ids that contain product on reserved list", response = Response.class)
    public Response<ProductInSectorView> getWarehouseIdsConatiningReservedProduct(@RequestParam(value = "productId", required = true) Long productId) {
        try {
            List<ProductInSectorView> productInSectors = warehouseSectorService
                    .findAllContainingReservedProductOrderedASCByProductId(productId)
                    .stream()
                    .map(sector -> ProductInSectorView.from(
                            sector.getId(),
                            productId,
                            warehouseSectorService.findQuantityOfReservedProductOnSectorByProductId(sector.getId(), productId)
                    ))
                    .collect(Collectors.toList());

            return new Response<>(true, productInSectors);
        } catch (Exception e) {
            return new Response<ProductInSectorView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/warehouseSector/getAllProductsOnSector", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all products on sector", response = Response.class)
    public Response<WarehouseSectorProductsView> getWarehouseSectorProducts(@RequestParam(value = "sectorId") Long sectorId) {
        try {
            List<WarehouseSectorProductsView> productsInSector = warehouseSectorService
                    .findAllProductWithQuantitiesOnSector(sectorId)
                    .stream()
                    .map(productWithQuantity -> WarehouseSectorProductsView.from(
                            productWithQuantity.getProduct().getId(),
                            productWithQuantity.getProduct().getName(),
                            productWithQuantity.getProduct().getPrice(),
                            productWithQuantity.getProduct().getCategory(),
                            productWithQuantity.getProduct().getDescription(),
                            sectorId,
                            productWithQuantity.getQuantity()
                    ))
                    .collect(Collectors.toList());

            return new Response<>(true, productsInSector);
        } catch (Exception e) {
            return new Response<WarehouseSectorProductsView>(false, Optional.of(e.getMessage()));
        }
    }
}
