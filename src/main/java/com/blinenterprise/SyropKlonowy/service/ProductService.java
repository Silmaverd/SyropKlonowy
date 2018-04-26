package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ProductService extends CrudService<Product, Long, ProductRepository>{
    // TODO: Zgeneralizować używając wyrażeń lambda

    public Product findOneByName(String name){
        Iterable<Product> searchList = repository.findAll();
        for(Product product : searchList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> findAllByName(String name) {
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getName().equals(name)) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }

    public ArrayList<Product> findByMinPrice(Double price){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getPrice() >= price) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }

    public ArrayList<Product> findByMaxPrice(Double price){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getPrice() <= price) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }

    public ArrayList<Product> findAllOfCategory(Category category){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getCategory().equals(category)) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }

    public ArrayList<Product> findAllFromDate(Date date){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getProductionDate().after(date)) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }

    public ArrayList<Product> findAllToDate(Date date){
        Iterable<Product> searchList = repository.findAll();
        ArrayList<Product> resultsList = new ArrayList<Product>();
        for(Product product : searchList) {
            if (product.getProductionDate().before(date)) {
                resultsList.add(product);
            }
        }
        return resultsList;
    }




}