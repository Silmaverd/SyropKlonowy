package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.SaleOrderedProduct;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderedProductService {
    @Autowired
    SaleOrderedProductRepository saleOrderedProductRepository;

    public SaleOrderedProduct save(SaleOrderedProduct saleOrderedProduct) {
        return saleOrderedProductRepository.save(saleOrderedProduct);
    }
}
