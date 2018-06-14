package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class WarehouseSectorView implements View {
    private Long id;
    private String name;
    private Integer maxAmountOfProducts;
    private List<AmountOfProduct> notReservedAmountOfProducts;
    private List<AmountOfProduct> reservedAmountOfProducts;
    private Integer currentAmountOfProducts;

    public static WarehouseSectorView from(WarehouseSector warehouseSector) {
        return new WarehouseSectorView(
                warehouseSector.getId(),
                warehouseSector.getName(),
                warehouseSector.getMaxAmountOfProducts(),
                warehouseSector.getNotReservedAmountOfProducts(),
                warehouseSector.getReservedAmountOfProducts(),
                warehouseSector.getCurrentAmountOfProducts());
    }

    public WarehouseSectorView(Long id, String name, Integer maxAmountOfProducts, Map<Long, AmountOfProduct> notReservedAmountOfProducts, Map<Long, AmountOfProduct> reservedAmountOfProducts, Integer currentAmountOfProducts) {
        this.id = id;
        this.name = name;
        this.maxAmountOfProducts = maxAmountOfProducts;
        this.notReservedAmountOfProducts = new ArrayList<>(notReservedAmountOfProducts.values());
        this.reservedAmountOfProducts = new ArrayList<>(reservedAmountOfProducts.values());
        this.currentAmountOfProducts = currentAmountOfProducts;
    }

    public static List<WarehouseSectorView> from(List<WarehouseSector> warehouseSectors) {
        ArrayList<WarehouseSectorView> warehouseSectorViewList = new ArrayList<WarehouseSectorView>();
        for (WarehouseSector warehouseSector : warehouseSectors) {
            WarehouseSectorView wsv = from(warehouseSector);
            warehouseSectorViewList.add(wsv);
        }
        return warehouseSectorViewList;
    }
}
