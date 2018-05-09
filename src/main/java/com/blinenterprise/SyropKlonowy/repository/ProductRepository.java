package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findAllByName(String name);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByPriceLessThanEqual(float price);

    List<Product> findAllByPriceGreaterThanEqual(float price);

    List<Product> findAllByPriceLessThanEqualAndCategory(float price, Category category);

    List<Product> findAllByPriceGreaterThanEqualAndCategory(float price, Category category);

    Optional<Product> findByCode(String code);
}
