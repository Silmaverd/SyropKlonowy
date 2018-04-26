package com.blinenterprise.SyropKlonowy.api;


import com.blinenterprise.SyropKlonowy.domain.Address;
import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.domain.Enterprise;
import com.blinenterprise.SyropKlonowy.service.AddressService;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.web.ClientView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
@Api
public class ClientApi {

    @Autowired
    ClientService clientService;

    @Autowired
    AddressService addressService;

    @RequestMapping(path = "/client/addClient", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a client", response = Response.class)
    public Response<ClientView> addClient(@RequestParam(value = "name", required = true)String name,@RequestParam(value = "company", required = true) String company,
                                          @RequestParam(value = "street", required = true) String street, @RequestParam(value = "buildingNumber", required = true) String buildingNumber,
                                          @RequestParam(value = "city", required = true) String city, @RequestParam(value = "zipCode", required = true) int zipCode,
                                          @RequestParam(value = "enterpriseType", required = true) Enterprise enterpriseType) {
        try{
            Address address = new Address(street, buildingNumber, city, zipCode);
            addressService.create(address);
            Client client = new Client(name, company,false, address, enterpriseType);
            clientService.create(client);
            return new Response<ClientView>(true, Optional.empty());
        }
        catch (Exception e){
            return new Response<ClientView>(false, Optional.of(e.getMessage()));
        }
    }

    @RequestMapping(path = "/client/getClient", method = {RequestMethod.GET})
    public Response<ClientView> getClient(@RequestParam(value = "name", required = true) String name) {
        return new Response<ClientView>(false, ClientView.from(null));
    }
}