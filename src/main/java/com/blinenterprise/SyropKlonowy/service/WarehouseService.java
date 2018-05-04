package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.SuppliedProduct;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private WarehouseRepository warehouseRepository;
    private ProductService productService;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository, ProductService productService) {
        this.warehouseRepository = warehouseRepository;
        this.productService = productService;
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    public List<Warehouse> findAll() {
        return Lists.newArrayList(warehouseRepository.findAll());
    }

    public Optional<Warehouse> findByName(String name) {
        return warehouseRepository.findByName(name);
    }

    public Warehouse saveOrUpdate(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public Warehouse addSuppliedProduct(SuppliedProduct suppliedProduct, String warehouseName) {
        Product product = suppliedProduct.getProduct();
        Optional<Product> productInStockOptional = productService.findByCode(product.getCode());
        Product productInStock = productInStockOptional.orElseGet(() -> productService.save(product));
        Optional<Warehouse> warehouseOptional = findByName(warehouseName);
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            warehouse.addAmountOfProduct(new AmountOfProduct(productInStock.getId(), suppliedProduct.getQuanity()));
            return saveOrUpdate(warehouse);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
