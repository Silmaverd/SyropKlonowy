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
@ToString
@NoArgsConstructor
public class ClientView implements View {

    private String firstName;
    private String lastName;

    private String company;

    private boolean isVerifiedStatus;

    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private Enterprise enterpriseType;


    public static ClientView from(Client client) {
        ClientView clientView = new ClientView();
        clientView.firstName = client.getFirstName();
        clientView.lastName = client.getLastName();
        clientView.company = client.getCompany();
        clientView.isVerifiedStatus = client.isVerifiedStatus();
        clientView.deliveryAddress = client.getDeliveryAddress();
        clientView.enterpriseType = client.getEnterpriseType();
        return clientView;
    }

}
