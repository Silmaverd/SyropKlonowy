package com.blinenterprise.SyropKlonowy.domain.Client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;

    private String company;

    @Temporal(TemporalType.DATE)
    private final Date creationDate = new Date();

    private boolean isVerifiedStatus;

    @OneToOne
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private Enterprise enterpriseType;

    public Client(String name, String company, boolean isVerifiedStatus, Address deliveryAddress, Enterprise enterpriseType) {
        this.name = name;
        this.company = company;
        this.isVerifiedStatus = isVerifiedStatus;
        this.deliveryAddress = deliveryAddress;
        this.enterpriseType = enterpriseType;
    }
}
