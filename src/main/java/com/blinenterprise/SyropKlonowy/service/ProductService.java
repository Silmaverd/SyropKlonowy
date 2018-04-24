package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService extends CrudService<Product, Long, ProductRepository>{

}