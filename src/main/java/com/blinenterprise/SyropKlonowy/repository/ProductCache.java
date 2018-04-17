package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.web.UserOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ProductCache {

    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository repository;
    private HashMap<Long, Product> productCashe = new HashMap<>();

    public void purgeCashe(UserOperation userOperation){
        log.info("Clearing product cache within process " + userOperation.processID.toString());
        productCashe = new HashMap<>();
    }

    public Product getProductForId(Long id){
        return productCashe.get(id);
    }

    public void refreshCache(UserOperation userOperation){
        try {
            log.info("Refreshing product cache within proces " + userOperation.processID.toString());
            Iterable<Product> products = repository.findAll();
            products.forEach(product -> {
                productCashe.put(product.getId(), product);
                log.info(product.getId().toString());
            });
        } catch (Exception e) {
            log.error("Failed to refresh product cache within process " + userOperation.processID.toString(), e);
        }
    }
}
