package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService{

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllByName(String name) {
        return Lists.newArrayList(productRepository.findAllByName(name));
    }


}