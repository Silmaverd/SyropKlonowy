package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Client.Address;
import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClientView implements View {

    private Long id;
    private String name;
    private String company;
    private Address deliveryAddress;
    private Enterprise enterpriseType;


    public static ClientView from(Client client) {return new ClientView(client.getName(), client.getCompany(), client.getDeliveryAddress(), client.getEnterpriseType(), client.getId());}

    private ClientView(String name, String company, Address deliveryAddress, Enterprise enterpriseType, Long id) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.deliveryAddress = deliveryAddress;
        this.enterpriseType = enterpriseType;
    }

    public static List<ClientView> from(List<Client> clients) {
        ArrayList<ClientView> clientViewList = new ArrayList<ClientView>();
        for (Client client : clients) {
            ClientView pv = from(client);
            clientViewList.add(pv);
        }
        return clientViewList;
    }
}
