package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findByName(String name);

    Product findById(long id);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByPriceLessThanEqual(Double price);

    List<Product> findAllByPriceGreaterThanEqual(Double price);

    List<Product> findAllByPriceLessThanEqualAndCategory(Double price, Category category);

    List<Product> findAllByPriceGreaterThanEqualAndCategory(Double price, Category category);
}
