package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.web.ExampleResponse;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/maintenance")
@Api(value = "Warehouse Managment API")
class MaintenanceApi {

    @RequestMapping(path = "/maintenance/param", method = {RequestMethod.GET})
    @ApiOperation(value = "Display a parameter", response = Response.class)
    public Response<ExampleResponse> displayAParameter(@RequestParam(value = "title", required = true) String param) {
        return new Response<ExampleResponse>(true, new ArrayList<ExampleResponse>);
    }
    // TODO: its an example method; remove after adding any other method

}