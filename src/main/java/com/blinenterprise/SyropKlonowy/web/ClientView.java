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


    public ClientView from(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.company = client.getCompany();
        this.isVerifiedStatus = client.isVerifiedStatus();
        this.deliveryAddress = client.getDeliveryAddress();
        this.enterpriseType = client.getEnterpriseType();
        return this;
    }

}
