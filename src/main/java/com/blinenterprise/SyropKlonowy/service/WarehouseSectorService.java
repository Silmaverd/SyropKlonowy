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

    public boolean addProductWithQuantityBySectorId(ProductWithQuantity productWithQuantity, Integer amountPlaced, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (!warehouseSector.isPossibleToAddNewProducts(amountPlaced)) {
            log.info("Couldn't add new product, sector has no place for that amount");
            return false;
        }
        Product product = productWithQuantity.getProduct();
        Product productInStock = productService.findByCode(product.getCode())
                .orElseGet(() -> productService.save(product));
        if (productWithQuantity.decreaseAmountBy(amountPlaced)) {
            if (warehouseSector.addAmountOfProduct(new AmountOfProduct(productInStock.getId(), amountPlaced))) {
                saveOrUpdate(warehouseSector);
                log.info("Added new product: " + productWithQuantity.getProduct().getId() + " quantity: " + productWithQuantity.getQuantity());
                return true;
            }
            productWithQuantity.increaseAmountBy(amountPlaced);
        }
        log.info("Couldn't add new product, wrong amount to place");
        return false;
    }

    private boolean reserveSaleOrderedAmountOfProductBySectorId(AmountOfProduct amountOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (!warehouseSector.isPossibleToRemoveProducts(amountOfProduct.getQuantity())) {
            log.info("Couldn't reserve product, sector has no enough amount");
            return false;
        }
        if (warehouseSector.reserveSaleOrderedAmountOfProducs(amountOfProduct)) {
            saveOrUpdate(warehouseSector);
            log.info("Reserved product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
            return true;
        }
        log.info("Couldn't reserve product, sector has no enough amount of product");
        return false;
    }

    public boolean removeAmountOfProductBySectorId(AmountOfProduct amountOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (!warehouseSector.isPossibleToRemoveProducts(amountOfProduct.getQuantity())) {
            log.info("Couldn't remove product, sector has no enough amount");
            return false;
        }
        if (warehouseSector.removeAmountOfProduct(amountOfProduct)) {
            saveOrUpdate(warehouseSector);
            log.info("Removed product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
            return true;
        }
        log.info("Couldn't remove product, sector has no enough amount of product");
        return false;
    }

    public void reserveSaleOrderedAmountOfProduct(AmountOfProduct orderedProduct) {
        List<WarehouseSector> warehouseSectors = findAll();
        Integer restOfProductQuantity = orderedProduct.getQuantity();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            Integer productQuantity = warehouseSector.getQuantityOfProductByIdIfExist(orderedProduct.getProductId());
            if (restOfProductQuantity <= productQuantity) {
                if(reserveSaleOrderedAmountOfProductBySectorId(new AmountOfProduct(orderedProduct.getProductId(), restOfProductQuantity), warehouseSector.getId())){
                    break;
                }
            } else if (productQuantity > 0) {
                restOfProductQuantity -= productQuantity;
                reserveSaleOrderedAmountOfProductBySectorId(new AmountOfProduct(orderedProduct.getProductId(), productQuantity), warehouseSector.getId());
            }
        }
    }

    public void unReserveAmountOfProduct(AmountOfProduct amountOfProduct) {
        List<WarehouseSector> warehouseSectors = findAll();
        Integer restOfProductQuantity = amountOfProduct.getQuantity();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            Integer productQuantity = warehouseSector.getSaleOrderQuantityOfProductByIdIfExist(amountOfProduct.getProductId());
            if (restOfProductQuantity <= productQuantity) {
                warehouseSector.unReserveAmountOfProductsFromSaleOrder(new AmountOfProduct(amountOfProduct.getProductId(), restOfProductQuantity));
                break;
            } else if (productQuantity > 0) {
                restOfProductQuantity -= productQuantity;
                warehouseSector.unReserveAmountOfProductsFromSaleOrder(new AmountOfProduct(amountOfProduct.getProductId(), productQuantity));
            }
        }
    }
}
