package com.blinenterprise.SyropKlonowy.api;


import com.blinenterprise.SyropKlonowy.domain.Address;
import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.domain.Enterprise;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.web.ClientView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
@Api
public class ClientApi {

    @Autowired
    ClientService clientService;

    @RequestMapping(path = "/client/add", method = {RequestMethod.PUT})
    @ApiOperation(value = "Add a client", response = Response.class)
    public Response<ClientView> AddClient(@PathVariable(value = "firstName")String firstName, @PathVariable(value = "lastName") String lastName,
                                          @PathVariable(value = "company") String company, @PathVariable(value = "street") String street,
                                          @PathVariable(value = "buildingNumber") String buildingNumber, @PathVariable(value = "city") String city,
                                          @PathVariable(value = "zipCode") int zipCode, @PathVariable(value = "enterpriseType") Enterprise enterpriseType) {
        Client client = new Client(firstName,lastName, company, new Date(), false, new Address(street, buildingNumber, city, zipCode), enterpriseType);
        if(clientService.create(client).equals(client)) {
            return new Response<ClientView>(true, Optional.empty());
        }
        else{
            return new Response<ClientView>(true, Optional.of("failure to create client"));
        }
    }

    @RequestMapping(path = "/client/show", method = {RequestMethod.GET})
    public Response<ClientView> showClient(@PathVariable(value = "firstName")String firstName, @PathVariable(value = "lastName") String lastName) {
        Client client = clientService.findByFistNameAndLastName(firstName, lastName);
        return new Response<ClientView>(false, ClientView.from(client));
    }
}