package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.repository.AmountOfProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmountOfProductService {
    @Autowired
    private AmountOfProductRepository amountOfProductRepository;

    public AmountOfProduct save(AmountOfProduct amountOfProduct) {
        return amountOfProductRepository.save(amountOfProduct);
    }
}
