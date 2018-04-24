package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.ProductLine;
import com.blinenterprise.SyropKlonowy.repository.ProductLineRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductLineService implements CRUDService<ProductLine, Long> {

    @Autowired
    private ProductLineRepository productLineRepository;

    @Override
    public ProductLine create(ProductLine productLine) {
        return productLineRepository.save(productLine);
    }

    @Override
    public ProductLine findById(Long id) {
        Optional<ProductLine> productLineById = productLineRepository.findById(id);
        return productLineById.orElse(null);
    }

    @Override
    public List<ProductLine> findAll() {
        return Lists.newArrayList(productLineRepository.findAll());
    }

    @Override
    public ProductLine update(ProductLine productLine) {
        return productLineRepository.save(productLine);
    }

    @Override
    public void delete(Long id) {
        productLineRepository.deleteById(id);
    }
}
