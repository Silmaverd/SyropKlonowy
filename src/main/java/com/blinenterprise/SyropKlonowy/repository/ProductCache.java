package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.events.Operation;
import com.blinenterprise.SyropKlonowy.events.UserOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class ProductCache {

    @Autowired
    private ProductRepository repository;
    private HashMap<Long, Product> productCashe = new HashMap<>();

    public void purgeCashe(Operation operation){
        operation.persist();
        productCashe = new HashMap<>();
    }

    public Product getProductForId(Long id){
        return productCashe.get(id);
    }

    public void refreshCache(Operation operation){
        try {
            operation.persist();
            Iterable<Product> products = repository.findAll();
            products.forEach(product -> {
                productCashe.put(product.getId(), product);
                log.info(product.getId().toString());
            });
        } catch (Exception e) {
            log.error("Failed to refresh product cache within process " + operation.getProcessID().toString(), e);
        }
    }
}
