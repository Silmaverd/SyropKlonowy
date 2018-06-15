package com.blinenterprise.SyropKlonowy.domain.Client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String street;
    private String buildingNumber;
    private String city;
    private String zipCode;

    public Address(String street, String buildingNumber, String city, String zipCode) {
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.zipCode = zipCode;
    }
}
