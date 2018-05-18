package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findAllByName(String name);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByPriceLessThanEqual(BigDecimal price);

    List<Product> findAllByPriceGreaterThanEqual(BigDecimal price);

    List<Product> findAllByPriceLessThanEqualAndCategory(BigDecimal price, Category category);

    List<Product> findAllByPriceGreaterThanEqualAndCategory(BigDecimal price, Category category);

    Optional<Product> findByCode(String code);

}
