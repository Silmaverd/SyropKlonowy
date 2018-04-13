package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class WebServer {

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return (evt) -> productRepository.saveAll(Arrays.asList(
                new Product("PC1", 155.56, Category.CATEGORY_1, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC"),
                new Product("Laptop1", 3563.42, Category.CATEGORY_2, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1"),
                new Product("Laptop3", 2000.30, Category.CATEGORY_2, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2"),
                new Product("Smarphone", 800.99, Category.CATEGORY_3, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop"),
                new Product("Speaker1", 5.12, Category.CATEGORY_4, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1"),
                new Product("Speaker2", 10.12, Category.CATEGORY_4, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2")
        ));
    }
}
