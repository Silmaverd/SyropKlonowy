package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import com.blinenterprise.SyropKlonowy.repository.WarehouseSectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        return warehouseSectorRepository.findByName(name);
    }

    public WarehouseSector saveOrUpdate(WarehouseSector warehouseSector) {
        return warehouseSectorRepository.save(warehouseSector);
    }

    public List<WarehouseSector> findAllContainingNotReservedProductOrderedASCByProductId(Long productId) {
        return warehouseSectorRepository.findAllContainingNotReservedProductOrderedASCByProductId(productId);
    }

    public List<WarehouseSector> findAllContainingReservedProductOrderedASCByProductId(Long productId) {
        return warehouseSectorRepository.findAllContainingReservedProductOrderedASCByProductId(productId);
    }

    public List<WarehouseSector> findAllContainingReservedProductOrderedDESCByProductId(Long productId) {
        return warehouseSectorRepository.findAllContainingReservedProductOrderedDESCByProductId(productId);
    }

    public Integer findQuantityOfReservedProductOnSectorByProductId(Long sectorId, Long productId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        return warehouseSector.getQuantityOfReservedProductByIdIfExist(productId);
    }

    public Integer findQuantityOfNotReservedProductOnSectorByProductId(Long sectorId, Long productId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        return warehouseSector.getQuantityOfNotReservedProductByIdIfExist(productId);
    }

    public List<AmountOfProduct> findAllAmountsOfProductOnSector(Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        Collection<AmountOfProduct> products = warehouseSector.getNotReservedAmountOfProducts().values();
        warehouseSector.getReservedAmountOfProducts().values().forEach(amountOfProduct -> {
            if(products.stream().anyMatch(product -> product.getProductId().equals(amountOfProduct.getProductId()))){
                products.stream()
                        .filter(product -> product.getProductId().equals(amountOfProduct.getProductId()))
                        .findFirst()
                        .get()
                        .increaseQuantityBy(amountOfProduct.getQuantity());
            } else {
                products.add(amountOfProduct);
            }
        });
        return new ArrayList<>(products);
    }

    public List<AmountOfProduct> findAllAmountsOfProductOnAllSectors() {
        Map<Long, AmountOfProduct> notReservedAmountOfProductsInAllSectors = new HashMap<>();
        findAll().forEach(warehouseSector ->
                warehouseSector.getNotReservedAmountOfProducts().forEach((aLong, amountOfProduct) -> {
                    notReservedAmountOfProductsInAllSectors.putIfAbsent(aLong, new AmountOfProduct(aLong, 0));
                    notReservedAmountOfProductsInAllSectors.get(aLong).increaseQuantityBy(amountOfProduct.getQuantity());
                }));
        return Lists.newArrayList(notReservedAmountOfProductsInAllSectors.values());
    }

    public List<ProductWithQuantity> findAllProductWithQuantitiesOnSector(Long sectorId) {
        List<AmountOfProduct> amountsOfProduct = findAllAmountsOfProductOnSector(sectorId);
        return amountsOfProduct.stream().map(amountOfProduct -> {
            return new ProductWithQuantity(productService.findById(amountOfProduct.getProductId()).orElseThrow(IllegalArgumentException::new),
                    amountOfProduct.getQuantity());
        }).collect(Collectors.toList());
    }

    public boolean addProductWithQuantityBySectorId(Product deliveredProduct, Integer quantityOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (warehouseSector.isPossibleToAddNewProducts(quantityOfProduct)) {
            Product productInStock = productService.findByCode(deliveredProduct.getCode())
                    .orElseGet(() -> productService.save(deliveredProduct));
            if (warehouseSector.addAmountOfProduct(new AmountOfProduct(productInStock.getId(), quantityOfProduct))) {
                saveOrUpdate(warehouseSector);
                log.info("Added new product: " + deliveredProduct.getId() + " quantity: " + quantityOfProduct);
                return true;
            }
        }
        log.info("Couldn't add new product, wrong amount to place");
        return false;
    }

    private boolean reserveAmountOfProductBySectorId(AmountOfProduct amountOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (warehouseSector.isPossibleToRemoveProducts(amountOfProduct.getQuantity()) &&
                warehouseSector.reserveAmountOfProduct(amountOfProduct)) {

            saveOrUpdate(warehouseSector);
            log.info("Reserved product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
            return true;
        }
        log.info("Couldn't reserve product, sector has no enough amount of product");
        return false;
    }

    public boolean removeAmountOfProductBySectorId(AmountOfProduct amountOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (warehouseSector.isPossibleToRemoveProducts(amountOfProduct.getQuantity()) &&
                warehouseSector.removeAmountOfProduct(amountOfProduct)) {

            saveOrUpdate(warehouseSector);
            log.info("Removed product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
            return true;
        }
        log.info("Couldn't remove product, sector has no enough amount of product");
        return false;
    }

    private boolean removeReservedAmountOfProductBySectorId(AmountOfProduct amountOfProduct, Long sectorId) {
        WarehouseSector warehouseSector = findById(sectorId).orElseThrow(IllegalArgumentException::new);
        if (warehouseSector.isPossibleToRemoveProducts(amountOfProduct.getQuantity()) &&
                warehouseSector.removeReservedAmountOfProduct(amountOfProduct)) {

            saveOrUpdate(warehouseSector);
            log.info("Removed ordered product: " + amountOfProduct.getProductId() + " quantity: " + amountOfProduct.getQuantity());
            return true;
        }
        log.info("Couldn't remove product, sector has no enough sale ordered amount of product");
        return false;
    }

    public void removeReservedAmountOfProduct(AmountOfProduct amountOfProduct) {
        List<WarehouseSector> warehouseSectors = findAllContainingReservedProductOrderedASCByProductId(amountOfProduct.getProductId());
        Integer restOfProductQuantity = amountOfProduct.getQuantity();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            Integer productQuantity = warehouseSector.getQuantityOfReservedProductByIdIfExist(amountOfProduct.getProductId());
            if (restOfProductQuantity <= productQuantity) {
                if (removeReservedAmountOfProductBySectorId(new AmountOfProduct(amountOfProduct.getProductId(), restOfProductQuantity), warehouseSector.getId())) {
                    break;
                }
            } else if (productQuantity > 0) {
                if (removeReservedAmountOfProductBySectorId(new AmountOfProduct(amountOfProduct.getProductId(), productQuantity), warehouseSector.getId())) {
                    restOfProductQuantity -= productQuantity;
                }
            }
        }
    }

    public void reserveAmountOfProduct(AmountOfProduct amountOfProduct) {
        List<WarehouseSector> warehouseSectors = findAllContainingNotReservedProductOrderedASCByProductId(amountOfProduct.getProductId());
        Integer restOfProductQuantity = amountOfProduct.getQuantity();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            Integer productQuantity = warehouseSector.getQuantityOfNotReservedProductByIdIfExist(amountOfProduct.getProductId());
            if (restOfProductQuantity <= productQuantity) {
                if (reserveAmountOfProductBySectorId(new AmountOfProduct(amountOfProduct.getProductId(), restOfProductQuantity), warehouseSector.getId())) {
                    break;
                }
            } else if (productQuantity > 0) {
                if (reserveAmountOfProductBySectorId(new AmountOfProduct(amountOfProduct.getProductId(), productQuantity), warehouseSector.getId())) {
                    restOfProductQuantity -= productQuantity;
                }
            }
        }
    }

    public void unReserveAmountOfProduct(AmountOfProduct amountOfProduct) {
        List<WarehouseSector> warehouseSectors = findAllContainingReservedProductOrderedDESCByProductId(amountOfProduct.getProductId());
        Integer restOfProductQuantity = amountOfProduct.getQuantity();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            Integer productQuantity = warehouseSector.getQuantityOfReservedProductByIdIfExist(amountOfProduct.getProductId());
            if (restOfProductQuantity <= productQuantity) {
                if (warehouseSector.unReserveAmountOfProduct(new AmountOfProduct(amountOfProduct.getProductId(), restOfProductQuantity))) {
                    break;
                }
            } else if (productQuantity > 0) {
                if (warehouseSector.unReserveAmountOfProduct(new AmountOfProduct(amountOfProduct.getProductId(), productQuantity))) {
                    restOfProductQuantity -= productQuantity;
                }
            }
        }
    }
}
