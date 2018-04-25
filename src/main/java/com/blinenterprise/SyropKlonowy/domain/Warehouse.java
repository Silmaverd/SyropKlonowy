package com.blinenterprise.SyropKlonowy.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private static Warehouse warehouseInstance = null;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<AmountOfProduct> productIdWithQuantities = new ArrayList<>();

    private Warehouse(){

    }

    public static synchronized Warehouse getWarehouseInstance(){
        if(warehouseInstance == null){
            warehouseInstance = new Warehouse();
        }
        return warehouseInstance;
    }

    public void addProductIdWithQuantity(AmountOfProduct amountOfProduct){
        productIdWithQuantities.add(amountOfProduct);
    }

    public void addAllProductIdWithQuantity(List<AmountOfProduct> productIdWithQuantities){
        this.productIdWithQuantities.addAll(productIdWithQuantities);
    }

    public void removeProductIdWithQuantity(AmountOfProduct amountOfProduct){
        productIdWithQuantities.remove(amountOfProduct);
    }
}
