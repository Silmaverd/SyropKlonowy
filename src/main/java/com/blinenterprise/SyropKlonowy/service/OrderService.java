package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Order;
import com.blinenterprise.SyropKlonowy.domain.OrderedProduct;
import com.blinenterprise.SyropKlonowy.repository.OrderRepository;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void addOrder(Order order){
        Double total = 0.0;
        for(OrderedProduct orderedProduct: order.getOrderedProducts()){

        }
    }

}
