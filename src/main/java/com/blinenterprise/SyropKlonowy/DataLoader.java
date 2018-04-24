package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.ProductLine;
import com.blinenterprise.SyropKlonowy.repository.ProductLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@Deprecated
@Component
public class DataLoader implements ApplicationRunner {

    private ProductLineRepository productLineRepository;

    @Autowired
    public DataLoader(ProductLineRepository productLineRepository) {
        this.productLineRepository = productLineRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        productLineRepository.saveAll(Arrays.asList(
                new ProductLine(new Product("PC1", 155.56, Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC"), 33),
                new ProductLine(new Product("Laptop1", 3563.42, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1"), 122),
                new ProductLine(new Product("Laptop3", 2000.30, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2"), 1212),
                new ProductLine(new Product("Smarphone", 800.99, Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop"), 22),
                new ProductLine(new Product("Speaker1", 5.12, Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1"), 12323),
                new ProductLine(new Product("Speaker2", 10.12, Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2"), 53)
        ));
    }
}
