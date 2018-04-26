package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    public void loadData() {
        Warehouse warehouse = new Warehouse();
        warehouseRepository.save(warehouse);

        List<Product> products = Arrays.asList(
                new Product("PC1", new BigDecimal(155.56), Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC"),
                new Product("Laptop1", new BigDecimal(3563.42), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1"),
                new Product("Laptop3", new BigDecimal(2000.30), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2"),
                new Product("Smarphone", new BigDecimal(800.99), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop"),
                new Product("Speaker1", new BigDecimal(5.12), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1"),
                new Product("Speaker2", new BigDecimal(10.12), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2")
        );
        productRepository.saveAll(products);

        List<AmountOfProduct> amountOfProducts = new ArrayList<>();
        products.forEach(product -> amountOfProducts.add(new AmountOfProduct(product.getId(), 20)));

        amountOfProducts.forEach(warehouse::addAmountOfProduct);
        warehouseRepository.save(warehouse);
    }
}
