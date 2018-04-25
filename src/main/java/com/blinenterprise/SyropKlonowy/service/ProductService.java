package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService extends CrudService<Product, Long, ProductRepository>{
    public Product findByName(String name){
        Iterable<Product> searchList = repository.findAll();
        for(Product product : searchList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

}