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

    private Long id;
    private String name;
    private String price;
    private Category category;
    private Date productionDate;
    private String description;
    private String code;

    private ProductView(String name, String price, Category category, Date productionDate, String description, Long id, String code) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.description = description;
        this.code = code;
    }

    public static ProductView from(Product product) {
        return new ProductView(
                product.getName(),
                MoneyConverter.getString(product.getPrice()),
                product.getCategory(),
                product.getProductionDate(),
                product.getDescription(),
                product.getId(),
                product.getCode());
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