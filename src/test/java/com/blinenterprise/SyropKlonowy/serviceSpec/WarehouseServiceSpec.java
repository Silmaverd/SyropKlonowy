package com.blinenterprise.SyropKlonowy.serviceSpec;

import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.SuppliedProduct;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
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

    private WarehouseRepository warehouseRepository = Mockito.mock(WarehouseRepository.class);
    private ProductService productService = Mockito.mock(ProductService.class);
    private WarehouseService warehouseService = new WarehouseService(warehouseRepository, productService);

    private final Product product = ProductBuilder.aProduct()
            .withCategory(Category.AUDIO)
            .withCode("XXX3")
            .withDescription("Opis produktu")
            .withName("produkt1")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(5)))
            .build();

    private final Product product2 = ProductBuilder.aProduct()
            .withCategory(Category.AUDIO)
            .withCode("XXX5")
            .withDescription("Opis produktu")
            .withName("produkt3")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(7)))
            .build();

    private final Integer PRODUCT_QUANTITY = 15;

    private final String WAREHOUSE_NAME = "Main";
    private Warehouse warehouse = new Warehouse(WAREHOUSE_NAME);


    @Before
    public void setUp() {
        Mockito.when(warehouseRepository.findByName(any(String.class))).thenReturn(Optional.of(warehouse));
        Mockito.when(productService.findByCode(product.getCode())).thenReturn(Optional.of(product));
        Mockito.when(productService.findByCode(product2.getCode())).thenReturn(Optional.of(product2));
        Mockito.when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);
    }

    @Test
    public void findByNameWarehouse() {
        Optional<Warehouse> w2 = warehouseService.findByName(WAREHOUSE_NAME);
        Assert.assertEquals(warehouse.getName(), w2.get().getName());
    }

    @Test
    public void shouldAddNewProduct() {
        product.setId(1L);
        SuppliedProduct suppliedProduct = new SuppliedProduct(product, PRODUCT_QUANTITY);

        Warehouse updatedWarehouse = warehouseService.addSuppliedProduct(suppliedProduct, WAREHOUSE_NAME);

        Assert.assertEquals(1, updatedWarehouse.getAmountOfProducts().size());
    }

    @Test
    public void shouldIncrementProductQuantity() {
        product.setId(1L);
        product2.setId(2L);
        SuppliedProduct suppliedProduct = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SuppliedProduct suppliedProduct2 = new SuppliedProduct(product, PRODUCT_QUANTITY);
        SuppliedProduct suppliedProduct3 = new SuppliedProduct(product2, PRODUCT_QUANTITY);

        warehouseService.addSuppliedProduct(suppliedProduct, WAREHOUSE_NAME);
        warehouseService.addSuppliedProduct(suppliedProduct2, WAREHOUSE_NAME);
        Warehouse updatedWarehouse = warehouseService.addSuppliedProduct(suppliedProduct3, WAREHOUSE_NAME);
        Integer productQuantity = updatedWarehouse.getAmountOfProducts().get(product.getId()).getQuantity();

        Assert.assertEquals(2, updatedWarehouse.getAmountOfProducts().size());
        Assert.assertEquals(productQuantity.toString(), "30");
    }
}
