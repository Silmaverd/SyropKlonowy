package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;




@Service
public class ProductService extends CrudService<Product, Long, ProductRepository>{

    public Product findOneByCriterion(SearchCriterion<Product> criterion){
        Iterable<Product> searchList = repository.findAll();
        for(Product product : searchList) {
            if (criterion.fulfilledBy(product)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> findAllByCriterion(SearchCriterion<Product> criterion){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (criterion.fulfilledBy(product)) {
                resultsList.add(product);
            }
        }
        if (resultsList.isEmpty()) {
            return null;
        } else {
            return resultsList;
        }
    }

    public Product findOneByName(String name){
        return findOneByCriterion(product -> product.getName().equals(name));
    }

    public ArrayList<Product> findAllByName(String name) {
        return findAllByCriterion(product -> product.getName().equals(name));
    }

    public ArrayList<Product> findByMinPrice(Double price){
        return findAllByCriterion(product -> product.getPrice() >= price);
    }

    public ArrayList<Product> findByMaxPrice(Double price){
        return findAllByCriterion(product -> product.getPrice() <= price);
    }

    public ArrayList<Product> findAllOfCategory(Category category){
        return findAllByCriterion(product -> product.getCategory().equals(category));
    }

    public ArrayList<Product> findAllAfterDate(Date date){
        return findAllByCriterion(product -> product.getProductionDate().after(date));
    }

    public ArrayList<Product> findAllBeforeDate(Date date){
        return findAllByCriterion(product -> product.getProductionDate().before(date));
    }




}