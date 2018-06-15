package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Client.Address;
import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.service.AddressService;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.view.ClientView;
import com.blinenterprise.SyropKlonowy.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                                          @RequestParam(value = "city", required = true) String city, @RequestParam(value = "zipCode", required = true) String zipCode,
                                          @RequestParam(value = "enterpriseType", required = true) Enterprise enterpriseType) {
        try{
            Address address = new Address(street, buildingNumber, city, zipCode);
            addressService.create(address);
            Client client = new Client(name, company,false, address, enterpriseType);
            clientService.create(client);
            return new Response<ClientView>(true, Optional.empty());
        }
        catch (Exception e){
            return new Response<ClientView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/getClient", method = {RequestMethod.GET})
    public Response<ClientView> getClient(@RequestParam(value = "name", required = true) String name) {
        try {

            List<Client> listOfClients = clientService.findByName(name);
            List<ClientView> returnList = new ArrayList<>();

            for(Client c : listOfClients){
                returnList.add(ClientView.from(c));
            }

            return new Response<ClientView>(true, returnList);
        }
        catch (Exception e){
            return new Response<ClientView>(false, Optional.of(e.toString()));
        }
    }

    @RequestMapping(path = "/client/getClientById", method = {RequestMethod.GET})
    public Response<ClientView> getClientById(@RequestParam(value = "clientId", required = true) Long clientId) {
        try {
            return new Response<ClientView>(true, Arrays.asList(ClientView.from(clientService.findById(clientId).orElseThrow(IllegalArgumentException::new))));
        }
        catch (Exception e){
            return new Response<ClientView>(false, Optional.of(e.getMessage()));
        }
    }


    @RequestMapping(path = "/client/getAll", method = {RequestMethod.GET})
    @ApiOperation(value = "Display all clients", response = Response.class)
    public Response<ClientView> getAllClients() {
        Response<ClientView> response;
        try {
            List<Client> result = clientService.findAll();
            response = new Response<ClientView>(true, ClientView.from(result));
        } catch (Exception e) {
            response = new Response<ClientView>(false, Optional.of(e.getMessage()));
        }
        return response;
    }
}