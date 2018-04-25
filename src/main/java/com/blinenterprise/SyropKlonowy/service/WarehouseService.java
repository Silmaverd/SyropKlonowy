package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService implements CRUDService<Warehouse, Long> {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public Warehouse create(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse findById(Long id) {
        Optional<Warehouse> warehouseById = warehouseRepository.findById(id);
        return warehouseById.orElse(null);
    }

    @Override
    public List<Warehouse> findAll() {
        return Lists.newArrayList(warehouseRepository.findAll());
    }

    @Override
    public Warehouse update(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }
}
