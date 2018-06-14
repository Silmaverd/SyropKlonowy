package com.blinenterprise.SyropKlonowy.service;


import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.repository.ProductWithQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductWithQuantityService {

    @Autowired
    private ProductWithQuantityRepository productWithQuantityRepository;

    @Autowired
    private ProductService productService;

    public ProductWithQuantity findById(Long id) {
        return productWithQuantityRepository.findById(id).orElse(null);
    }

    public void save(ProductWithQuantity productWithQuantity){
        productService.save(productWithQuantity.getProduct());
        productWithQuantityRepository.save(productWithQuantity);
    }

}
