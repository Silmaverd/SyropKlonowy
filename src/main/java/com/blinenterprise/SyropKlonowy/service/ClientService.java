package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client create(Client client){return clientRepository.save(client);}

    public Client findById(long id){
        Optional<Client> mayBeClient= clientRepository.findById(id);
        return mayBeClient.orElse(null);
    }

    public Iterable<Client> findByName(String name){
        return clientRepository.findAllByName(name);
    }
}
