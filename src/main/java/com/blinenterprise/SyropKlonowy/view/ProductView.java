package com.blinenterprise.SyropKlonowy.view;


import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class ProductView implements View {


    private String name;
    private Double price;
    private Category category;
    private Date productionDate;
    private String description;

    public static ProductView from(Product product) {
        ProductView pv = new ProductView();
        pv.name = product.getName();
        pv.price = product.getPrice();
        pv.category = product.getCategory();
        pv.productionDate = product.getProductionDate();
        pv.description = product.getDescription();
        return pv;
    }


}