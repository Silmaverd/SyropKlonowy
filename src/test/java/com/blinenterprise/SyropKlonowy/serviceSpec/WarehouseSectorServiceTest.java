package com.blinenterprise.SyropKlonowy.serviceSpec;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector;
import com.blinenterprise.SyropKlonowy.domain.builder.ProductBuilder;
import com.blinenterprise.SyropKlonowy.domain.builder.WarehouseSectorBuilder;
import com.blinenterprise.SyropKlonowy.repository.WarehouseSectorRepository;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WarehouseSectorServiceTest {

    private WarehouseSectorRepository warehouseSectorRepositoryMock = Mockito.mock(WarehouseSectorRepository.class);
    private ProductService productServiceMock = Mockito.mock(ProductService.class);
    private WarehouseSectorService warehouseSectorService = new WarehouseSectorService(warehouseSectorRepositoryMock, productServiceMock);

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

    private final Long WAREHOUSE_SECTOR_ID = 1L;
    private WarehouseSector warehouseSector = WarehouseSectorBuilder.aWarehouseSector()
            .withId(WAREHOUSE_SECTOR_ID)
            .withName("MAIN")
            .withMaxAmountOfProducts(50)
            .build();

    @Before
    public void setUp() {
        Mockito.when(warehouseSectorRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(warehouseSector));
        Mockito.when(productServiceMock.findByCode(product.getCode())).thenReturn(Optional.of(product));
        Mockito.when(productServiceMock.findByCode(product2.getCode())).thenReturn(Optional.of(product2));
        Mockito.when(warehouseSectorRepositoryMock.save(any(WarehouseSector.class))).thenReturn(warehouseSector);
        Mockito.when(productServiceMock.findById(any(Long.class))).thenReturn(Optional.of(product));
        Mockito.when(warehouseSectorRepositoryMock.findAll()).thenReturn(Lists.newArrayList(warehouseSector));
    }

    @Test
    public void shouldAddNewProduct() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);

        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);

        Assert.assertEquals(1, warehouseSector.getAmountOfProducts().size());
    }

    @Test
    public void shouldIncreaseProductQuantity() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity poductWithQuantity2 = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(product2, PRODUCT_QUANTITY);

        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(poductWithQuantity2, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity3, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);

        Integer actualProductQuantity = warehouseSector.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 30;

        Assert.assertEquals(2, warehouseSector.getAmountOfProducts().size());
        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }


    @Test
    public void shouldReturnFalseWhenIncreaseProductQuantityOverMaxAmountOfWarehouseSector() {
        Integer overMaxAmountOfWarehouseSector = 100;
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, overMaxAmountOfWarehouseSector);

        boolean result = warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, overMaxAmountOfWarehouseSector, WAREHOUSE_SECTOR_ID);

        Assert.assertFalse(result);
    }


    @Test
    public void shouldDecreaseProductQuantity() {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(product, PRODUCT_QUANTITY);
        AmountOfProduct amountOfProduct = new AmountOfProduct(product.getId(), 30);

        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, PRODUCT_QUANTITY,WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity2, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.removeAmountOfProductBySectorId(amountOfProduct, WAREHOUSE_SECTOR_ID);

        Integer actualProductQuantity = warehouseSector.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 0;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }

    @Test
    public void shouldReserveAmountOfProduct(){
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, 20);
        ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(product2, 20);
        AmountOfProduct amountOfProduct = new AmountOfProduct(product.getId(), 15);
        AmountOfProduct amountOfProduct2 = new AmountOfProduct(product2.getId(), 20);

        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, 20,WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity2, 20, WAREHOUSE_SECTOR_ID);

        warehouseSectorService.reserveSaleOrderedAmountOfProduct(amountOfProduct);
        warehouseSectorService.reserveSaleOrderedAmountOfProduct(amountOfProduct2);

        Integer actualProductQuantity = warehouseSector.getAmountOfProducts().get(product.getId()).getQuantity();
        Integer actualProductQuantity1 = warehouseSector.getSaleOrderedAmountOfProducts().get(product.getId()).getQuantity();
        Integer actualProductQuantity2 = warehouseSector.getSaleOrderedAmountOfProducts().get(product2.getId()).getQuantity();
        Integer expectedProductQuantity = 20;
        Integer expectedProductQuantity1 = 15;
        Integer expectedProductQuantity2 = 20;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
        Assert.assertEquals(expectedProductQuantity1, actualProductQuantity1);
        Assert.assertEquals(expectedProductQuantity2, actualProductQuantity2);
    }

    @Test
    public void shouldRemoveReservedAmountOfProduct(){
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product, 20);
        AmountOfProduct reserveAmountOfProduct = new AmountOfProduct(product.getId(), 15);
        AmountOfProduct removeReservedAmountOfProduct = new AmountOfProduct(product.getId(), 15);

        warehouseSectorService.addProductWithQuantityBySectorId(productWithQuantity, 20,WAREHOUSE_SECTOR_ID);
        warehouseSectorService.reserveSaleOrderedAmountOfProduct(reserveAmountOfProduct);
        warehouseSectorService.removeAmountOfProductBySectorId(removeReservedAmountOfProduct, WAREHOUSE_SECTOR_ID);

        Integer actualProductQuantityInAmountOfProducts = warehouseSector.getAmountOfProducts().get(product.getId()).getQuantity();
        Boolean isContaining = warehouseSector.getSaleOrderedAmountOfProducts().containsKey(product.getId());
        Integer currentAmount = warehouseSector.getCurrentAmountOfProducts();

        Integer expectedProductQuantity = 5;
        Integer expectedCurrentAmount = 5;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantityInAmountOfProducts);
        Assert.assertFalse(isContaining);
        Assert.assertEquals(expectedCurrentAmount, currentAmount);
    }


}
