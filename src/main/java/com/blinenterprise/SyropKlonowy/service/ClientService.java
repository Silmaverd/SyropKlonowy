package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Client;
import com.blinenterprise.SyropKlonowy.repository.ClientRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public void create(Client client){clientRepository.save(client);}

    public Optional<Client> findById(long id){
        return clientRepository.findById(id);
    }

    public List<Client> findByName(String name){
        return Lists.newArrayList(clientRepository.findAllByName(name));
    }

}
