package com.blinenterprise.SyropKlonowy.web;

import com.blinenterprise.SyropKlonowy.domain.Address;
import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.domain.Enterprise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
