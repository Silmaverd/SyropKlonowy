package com.blinenterprise.SyropKlonowy.service;


import com.blinenterprise.SyropKlonowy.domain.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.repository.ProductWithQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductWithQuantityService {

    @Autowired
    private ProductWithQuantityRepository productWithQuantityRepository;

    public ProductWithQuantity findById(Long id) {
        return productWithQuantityRepository.findById(id).orElse(null);
    }

}
