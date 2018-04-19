package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean exists(Long id){
        return this.productRepository.existsById(id);
    }

    public List<Product> findAll(){
        return StreamSupport.stream( productRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    public void save(Product product) {
        this.productRepository.save(product);
    }

    public Long count(){
        return this.productRepository.count();
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }



}
