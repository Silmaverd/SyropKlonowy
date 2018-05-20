package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.Client.Address;
import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import lombok.Getter;

@Getter
public class ClientView implements View {

    private String name;
    private String company;
    private Address deliveryAddress;
    private Enterprise enterpriseType;


    public static ClientView from(Client client) {return new ClientView(client.getName(), client.getCompany(), client.getDeliveryAddress(), client.getEnterpriseType());}

    private ClientView(String name, String company, Address deliveryAddress, Enterprise enterpriseType) {
        this.name = name;
        this.company = company;
        this.deliveryAddress = deliveryAddress;
        this.enterpriseType = enterpriseType;
    }
}
