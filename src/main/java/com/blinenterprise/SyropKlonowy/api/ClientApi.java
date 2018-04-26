package com.blinenterprise.SyropKlonowy.api;


import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.web.ClientView;
import com.blinenterprise.SyropKlonowy.web.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/api/client")
@Api
public class ClientApi {

    @Autowired
    ClientService clientService;

    @RequestMapping(path = "/client/add", method = {RequestMethod.GET})
    @ApiOperation(value = "Display a parameter", response = Response.class)
    public Response<ClientView> AddClient(@RequestBody ClientView clientView) {
        Client client = new Client(clientView.getFirstName(),clientView.getLastName(), clientView.getCompany(), new Date(), false, clientView.getDeliveryAddress(), clientView.getEnterpriseType());
        clientService.create(client);
        return new Response<ClientView>(true, new ClientView());
    }

    @RequestMapping(path = "/client/show", method = {RequestMethod.GET})
    public Response<ClientView> showClient(@PathVariable(value = "firstName")String firstName, @PathVariable(value = "lastName") String lastName) {// @PathVariable(value = "id") Long id)
        Client client = clientService.findByFistNameAndLastName(firstName, lastName);
        return new Response<ClientView>(true, ClientView.from(client));
    }
}
