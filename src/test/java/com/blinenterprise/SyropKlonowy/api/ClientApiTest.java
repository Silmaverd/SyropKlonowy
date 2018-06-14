package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Client.Address;
import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.service.AddressService;
import com.blinenterprise.SyropKlonowy.service.ClientService;
import com.blinenterprise.SyropKlonowy.view.ClientView;
import com.blinenterprise.SyropKlonowy.view.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientApiTest {

    @InjectMocks
    private ClientApi clientApi;

    @Mock
    private AddressService addressService;
    @Mock
    private ClientService clientService;

    @Test
    public void addClientTest(){
        List<Client> mockedClientTable = new ArrayList<>();
        List<Address> mockedAddressTable = new ArrayList<>();
        Mockito.doAnswer(a -> {
            mockedAddressTable.add(a.getArgument(0));
            return null;
        }).when(addressService).create(ArgumentMatchers.any());
        Mockito.doAnswer(a -> {
            mockedClientTable.add(a.getArgument(0));
            return null;
        }).when(clientService).create(ArgumentMatchers.any());

        clientApi.addClient("Name1", "comp1", "Rac", "1", "Lublin", 20123, Enterprise.PRIVATE_PERSON);
        clientApi.addClient("Name2", "comp2", "Al", "2", "Lublin", 20123, Enterprise.PRIVATE_PERSON);

        Assert.assertTrue(mockedAddressTable.stream().anyMatch(address -> address.getCity().equals("Lublin")));
        Assert.assertTrue(mockedAddressTable.stream().anyMatch(address -> address.getBuildingNumber().equals("2")));

        Assert.assertTrue(mockedClientTable.stream().anyMatch(client -> client.getName().equals("Name1")));
        Assert.assertTrue(mockedClientTable.stream().anyMatch(client -> client.getName().equals("Name2")));
    }

    @Test
    public void getClientTest(){
        Client expected = new Client("Name1", "comp", false, new Address("ul", "1", "Lub", 20123), Enterprise.PRIVATE_PERSON);
        Mockito.doAnswer(a -> Arrays.asList(expected)).when(clientService).findByName(ArgumentMatchers.any());

        Response<ClientView> actualResponse = clientApi.getClient("Name1");

        Assert.assertEquals(expected.getName(), actualResponse.getPayload().get(0).getName());
    }
}
