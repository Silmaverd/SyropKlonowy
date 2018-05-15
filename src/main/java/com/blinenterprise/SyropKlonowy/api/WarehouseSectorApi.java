package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.blinenterprise.SyropKlonowy.view.WarehouseSectorView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                                                            @RequestParam(value = "maxAmount") Integer maxAmount) {
        try {
            WarehouseSector warehouseSector = new WarehouseSector(name, maxAmount);
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
}
