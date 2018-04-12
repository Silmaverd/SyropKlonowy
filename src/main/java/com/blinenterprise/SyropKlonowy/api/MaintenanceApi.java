package com.blinenterprise.SyropKlonowy.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maintenance")
@Api(value = "Library Managment API")
class MaintenanceApi {

    @RequestMapping(path = "/maintenance/param", method = {RequestMethod.GET})
    @ApiOperation(value = "Display a parameter")
    public String displayAParameter(@RequestParam(value = "title", required = true) String param) {
        return param;
    }
    // TODO: its an example method; remove after adding any other method

}