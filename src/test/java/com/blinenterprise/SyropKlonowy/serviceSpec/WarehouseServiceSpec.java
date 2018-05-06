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
            .withId(1L)
            .withCategory(Category.AUDIO)
            .withCode("XXX3")
            .withDescription("Opis produktu")
            .withName("produkt1")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(5)))
            .buildWithId();

    private Product product2 = ProductBuilder.aProduct()
            .withId(2L)
            .withCategory(Category.SPEAKER)
            .withCode("XXX5")
            .withDescription("Opis produktu")
            .withName("produkt2")
            .withPrice(new BigDecimal(20))
            .withProductionDate(Date.valueOf(LocalDate.now().minusWeeks(7)))
            .buildWithId();

    private final Integer PRODUCT_QUANTITY = 15;

    private final String WAREHOUSE_NAME = "Main";
    private Warehouse warehouse = new Warehouse(WAREHOUSE_NAME);

    @Before
    public void setUp() {
        Mockito.when(warehouseRepositoryMock.findByName(any(String.class))).thenReturn(Optional.of(warehouse));
        Mockito.when(productServiceMock.findByCode(product.getCode())).thenReturn(Optional.of(product));
        Mockito.when(productServiceMock.findByCode(product2.getCode())).thenReturn(Optional.of(product2));
        Mockito.when(warehouseRepositoryMock.save(any(Warehouse.class))).thenReturn(warehouse);
        Mockito.when(productServiceMock.findById(any(Long.class))).thenReturn(Optional.of(product));
    }


    @Test
    public void shouldAddNewProduct() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);

        warehouseService.addProductWithQuantity(productWithQuantity, WAREHOUSE_NAME);

        Assert.assertEquals(1, warehouse.getAmountOfProducts().size());
    }

    @Test
    public void shouldIncreaseProductQuantity() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity poductWithQuantity2 = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(product2, PRODUCT_QUANTITY);

        warehouseService.addProductWithQuantity(productWithQuantity, WAREHOUSE_NAME);
        warehouseService.addProductWithQuantity(poductWithQuantity2, WAREHOUSE_NAME);
        warehouseService.addProductWithQuantity(productWithQuantity3, WAREHOUSE_NAME);

        Integer actualProductQuantity = warehouse.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 30;

        Assert.assertEquals(2, warehouse.getAmountOfProducts().size());
        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }

    @Test
    public void shouldDecreaseProductQuantity() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        SaleOrderedProduct saleOrderedProduct = new SaleOrderedProduct(product.getId(), 30);

        warehouseService.addProductWithQuantity(productWithQuantity, WAREHOUSE_NAME);
        warehouseService.addProductWithQuantity(productWithQuantity2, WAREHOUSE_NAME);
        warehouseService.removeSaleOrderedProduct(saleOrderedProduct, WAREHOUSE_NAME);

        Integer actualProductQuantity = warehouse.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 0;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }
}
