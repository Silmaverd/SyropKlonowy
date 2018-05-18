package com.blinenterprise.SyropKlonowy.view;


import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.converter.MoneyConverter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class ProductView implements View {


    private String name;
    private String price;
    private Category category;
    private Date productionDate;
    private String description;


    private ProductView(String name, String price, Category category, Date productionDate, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
    }

    public static ProductView from(Product product) {
        return new ProductView(
                product.getName(),
                MoneyConverter.getString(product.getPrice()),
                product.getCategory(),
                product.getProductionDate(),
                product.getDescription());
    }

    public static List<ProductView> from(List<Product> products) {
        ArrayList<ProductView> productViewList = new ArrayList<ProductView>();
        for (Product product : products) {
            ProductView pv = from(product);
            productViewList.add(pv);
        }
        return productViewList;
    }


}