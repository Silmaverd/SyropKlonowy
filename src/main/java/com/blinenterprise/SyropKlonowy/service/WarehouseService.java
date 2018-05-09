package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.*;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        return warehouseRepository.findByName(name.toUpperCase());
    }

    public Warehouse saveOrUpdate(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void addProductWithQuantity(ProductWithQuantity productWithQuantity, String warehouseName) {
        Product product = productWithQuantity.getProduct();
        Optional<Product> productInStockOptional = productService.findByCode(product.getCode());
        Product productInStock = productInStockOptional.orElseGet(() -> productService.save(product));
        Optional<Warehouse> warehouseOptional = findByName(warehouseName);
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            warehouse.addAmountOfProduct(AmountOfProduct.fromProductWithQuantity(productWithQuantity));
            saveOrUpdate(warehouse);
            log.info("Added new product: " + productWithQuantity.getProduct().getId() + " quantity: " + productWithQuantity.getQuantity());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void removeSaleOrderedProduct(ProductWithQuantity productWithQuantity, String warehouseName) {
        Optional<Product> productInStockOptional = productService.findById(productWithQuantity.getProduct().getId());
        Optional<Warehouse> warehouseOptional = findByName(warehouseName);
        if (warehouseOptional.isPresent() && productInStockOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            warehouse.removeAmountOfProduct(new AmountOfProduct(
                    productInStockOptional.get().getId(),
                    productWithQuantity.getQuantity()));
            saveOrUpdate(warehouse);
            log.info("Removed product: " + productWithQuantity.getProduct().getId() + " quantity: " + productWithQuantity.getQuantity());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
