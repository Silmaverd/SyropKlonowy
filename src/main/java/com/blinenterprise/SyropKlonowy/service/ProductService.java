package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByName(String name) {
        return Lists.newArrayList(productRepository.findAllByName(name));
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> findAll() {
        return Lists.newArrayList(productRepository.findAll());
    }


}