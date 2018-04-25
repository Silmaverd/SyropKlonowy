package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

}
