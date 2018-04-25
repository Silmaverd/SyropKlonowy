package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String firstName;
    private String lastName;

    private String company;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    private boolean isVerifiedStatus;

    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private Enterprise enterpriseType;

    public Client(String firstName, String lastName, String company, Date creationDate, boolean isVerifiedStatus, Address deliveryAddress, Enterprise enterpriseType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.creationDate = creationDate;
        this.isVerifiedStatus = isVerifiedStatus;
        this.deliveryAddress = deliveryAddress;
        this.enterpriseType = enterpriseType;
    }
}
