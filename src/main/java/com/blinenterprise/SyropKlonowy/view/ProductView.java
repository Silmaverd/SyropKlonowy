package com.blinenterprise.SyropKlonowy.view;


import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class ProductView implements View {


    private String name;
    private BigDecimal price;
    private Category category;
    private Date productionDate;
    private String description;


    private ProductView(String name, BigDecimal price, Category category, Date productionDate, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
    }

    public static ProductView from(Product product) {
        return new ProductView(
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getProductionDate(),
                product.getDescription());
    }

    public static List<ProductView> from(List<Product> products) {
        ArrayList<ProductView> productViewList = new ArrayList<ProductView>();
        for (Product product : products) {
            ProductView pv = new ProductView(product.getName(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getProductionDate(),
                    product.getDescription());
            productViewList.add(pv);
        }
        return productViewList;
    }


}