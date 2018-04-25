package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    Client findByClient(long id){return clientRepository.findById(id);}

}
