package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    public SaleOrder create(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    public SaleOrder findById(Long id) {
        Optional<SaleOrder> warehouseById = saleOrderRepository.findById(id);
        return warehouseById.orElse(null);
    }

    public List<SaleOrder> findAll() {
        return Lists.newArrayList(saleOrderRepository.findAll());
    }

    public void deleteById(Long id) {
        saleOrderRepository.deleteById(id);
    }
}
