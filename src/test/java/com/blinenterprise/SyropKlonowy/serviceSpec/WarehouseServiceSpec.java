package com.blinenterprise.SyropKlonowy.serviceSpec;

import com.blinenterprise.SyropKlonowy.domain.*;
import com.blinenterprise.SyropKlonowy.domain.builder.ProductBuilder;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.WarehouseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WarehouseServiceSpec {

    private WarehouseRepository warehouseRepositoryMock = Mockito.mock(WarehouseRepository.class);
    private ProductService productServiceMock = Mockito.mock(ProductService.class);
    private WarehouseService warehouseService = new WarehouseService(warehouseRepositoryMock, productServiceMock);

    private Product product = ProductBuilder.aProduct()
            .withCategory(Category.AUDIO)
            .withCode("XXX3")
            .withDescription("Opis produktu")
            .withName("produkt1")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(5)))
            .build();

    private Product product2 = ProductBuilder.aProduct()
            .withCategory(Category.SPEAKER)
            .withCode("XXX5")
            .withDescription("Opis produktu")
            .withName("produkt2")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(7)))
            .build();

    private final Integer PRODUCT_QUANTITY = 15;

    private final String WAREHOUSE_NAME = "Main";
    private Warehouse warehouse = new Warehouse(WAREHOUSE_NAME);

    @Before
    public void setUp() {
        product.setId(1L);
        product2.setId(2L);
        Mockito.when(warehouseRepositoryMock.findByName(any(String.class))).thenReturn(Optional.of(warehouse));
        Mockito.when(productServiceMock.findByCode(product.getCode())).thenReturn(Optional.of(product));
        Mockito.when(productServiceMock.findByCode(product2.getCode())).thenReturn(Optional.of(product2));
        Mockito.when(warehouseRepositoryMock.save(any(Warehouse.class))).thenReturn(warehouse);
        Mockito.when(productServiceMock.findById(any(Long.class))).thenReturn(Optional.of(product));
    }


    @Test
    public void shouldAddNewProduct() {
        SuppliedProduct suppliedProduct = new SuppliedProduct(product, PRODUCT_QUANTITY);

        Warehouse updatedWarehouse = warehouseService.addSuppliedProduct(suppliedProduct, WAREHOUSE_NAME);

        Assert.assertEquals(1, updatedWarehouse.getAmountOfProducts().size());
    }

    @Test
    public void shouldIncrementProductQuantity() {
        SuppliedProduct suppliedProduct = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SuppliedProduct suppliedProduct2 = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SuppliedProduct suppliedProduct3 = new SuppliedProduct(product2, PRODUCT_QUANTITY);

        warehouseService.addSuppliedProduct(suppliedProduct, WAREHOUSE_NAME);
        warehouseService.addSuppliedProduct(suppliedProduct2, WAREHOUSE_NAME);
        Warehouse updatedWarehouse = warehouseService.addSuppliedProduct(suppliedProduct3, WAREHOUSE_NAME);

        Integer actualProductQuantity = updatedWarehouse.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 30;

        Assert.assertEquals(2, updatedWarehouse.getAmountOfProducts().size());
        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }

    @Test
    public void shouldDecreaseProductQuantity() {
        SuppliedProduct suppliedProduct = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SuppliedProduct suppliedProduct2 = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SaleOrderedProduct saleOrderedProduct = new SaleOrderedProduct(product.getId(), 30);

        warehouseService.addSuppliedProduct(suppliedProduct, WAREHOUSE_NAME);
        warehouseService.addSuppliedProduct(suppliedProduct2, WAREHOUSE_NAME);
        Warehouse updatedWarehouse = warehouseService.removeSaleOrderedProduct(saleOrderedProduct, WAREHOUSE_NAME);

        Integer actualProductQuantity = updatedWarehouse.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 0;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }
}
