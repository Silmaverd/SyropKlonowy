package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ProductService extends CrudService<Product, Long, ProductRepository>{

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllByName(String name) {
        return productRepository.findAllByName(name);
    }


}