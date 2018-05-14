package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import com.blinenterprise.SyropKlonowy.repository.WarehouseSectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WarehouseSectorService {

    private WarehouseSectorRepository warehouseSectorRepository;
    private ProductService productService;

    @Autowired
    public WarehouseSectorService(WarehouseSectorRepository warehouseSectorRepository, ProductService productService) {
        this.warehouseSectorRepository = warehouseSectorRepository;
        this.productService = productService;
    }

    public Optional<WarehouseSector> findById(Long id) {
        return warehouseSectorRepository.findById(id);
    }

    public List<WarehouseSector> findAll() {
        return Lists.newArrayList(warehouseSectorRepository.findAll());
    }

    public Optional<WarehouseSector> findByName(String name) {
        return warehouseSectorRepository.findByName(name.toUpperCase());
    }

    public WarehouseSector saveOrUpdate(WarehouseSector warehouseSector) {
        return warehouseSectorRepository.save(warehouseSector);
    }

    public void addProductWithQuantityBySectorId(ProductWithQuantity productWithQuantity, Long sectorId) {
        Product product = productWithQuantity.getProduct();
        Product productInStock = productService.findByCode(product.getCode())
                .orElseGet(() -> productService.save(product));
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        warehouseSector.addAmountOfProduct(AmountOfProduct.fromProductWithQuantity(productWithQuantity));
        saveOrUpdate(warehouseSector);
        log.info("Added new product: " + productWithQuantity.getProduct().getId() + " quantity: " + productWithQuantity.getQuantity());
    }

    public void removeAmountOfProduct(AmountOfProduct amountOfProduct, String warehouseName) {
        Optional<Product> productInStockOptional = productService.findById(amountOfProduct.getProductId());
        Optional<WarehouseSector> warehouseOptional = findByName(warehouseName);
        if (warehouseOptional.isPresent() && productInStockOptional.isPresent()) {
            WarehouseSector warehouseSector = warehouseOptional.get();
            warehouseSector.removeAmountOfProduct(amountOfProduct);
            saveOrUpdate(warehouseSector);
            log.info("Removed product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
