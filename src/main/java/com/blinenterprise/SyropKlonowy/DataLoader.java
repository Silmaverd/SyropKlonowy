package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.ProductLine;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
@Component
public class DataLoader {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    public void run() {
        List<Product> products = Arrays.asList(
                new Product("PC1", 155.56, Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC"),
                new Product("Laptop1", 3563.42, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1"),
                new Product("Laptop3", 2000.30, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2"),
                new Product("Smarphone", 800.99, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop"),
                new Product("Speaker1", 5.12, Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1"),
                new Product("Speaker2", 10.12, Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2")
        );
        productRepository.saveAll(products);

        List<ProductLine> productLines = new ArrayList<>();
        products.forEach(product -> productLines.add(new ProductLine(product.getId(), 20)));

        Warehouse warehouse = new Warehouse(productLines);
        warehouseRepository.save(warehouse);
    }
}
