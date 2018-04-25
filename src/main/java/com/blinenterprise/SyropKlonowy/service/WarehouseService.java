package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    public Warehouse findById(Long id) {
        Optional<Warehouse> warehouseById = warehouseRepository.findById(id);
        return warehouseById.orElse(null);
    }

    public List<Warehouse> findAll() {
        return Lists.newArrayList(warehouseRepository.findAll());
    }
}
