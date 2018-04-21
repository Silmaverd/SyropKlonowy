package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})

@DatabaseSetup(ProductRepositoryTests.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ProductRepositoryTests.DATASET })
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTests {

    protected static final String DATASET = "classpath:datasets/productRepository-products.xml";
    @Autowired
    ProductRepository productRepository;

    @Test
    public void findAllByPriceLessThanEqualAndCategory() {
        Assert.assertTrue(
                "Method should find 2 object",
                productRepository.findAllByPriceLessThanEqualAndCategory(5.0, Category.PHONE).size() == 2);
        Assert.assertTrue(
                "Method should find 1 object",
                productRepository.findAllByPriceLessThanEqualAndCategory(4.0, Category.PHONE).size() == 1);
    }
}
