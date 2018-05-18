package com.blinenterprise.SyropKlonowy.serviceSpec;

import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import com.blinenterprise.SyropKlonowy.domain.Product.ProductBuilder;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSectorBuilder;
import com.blinenterprise.SyropKlonowy.repository.WarehouseSectorRepository;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.service.WarehouseSectorService;
import com.google.common.collect.Lists;
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
        Mockito.when(warehouseSectorRepositoryMock.findAllContainingNotReservedProductOrderedASCByProductId(any(Long.class))).thenReturn(Lists.newArrayList(warehouseSector));
        Mockito.when(warehouseSectorRepositoryMock.findAllContainingReservedProductOrderedASCByProductId(any(Long.class))).thenReturn(Lists.newArrayList(warehouseSector));
        Mockito.when(warehouseSectorRepositoryMock.findAllContainingReservedProductOrderedDESCByProductId(any(Long.class))).thenReturn(Lists.newArrayList(warehouseSector));
    }

    @Test
    public void shouldAddNewProduct() {
        warehouseSectorService.addProductWithQuantityBySectorId(product, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);

        Assert.assertEquals(1, warehouseSector.getNotReservedAmountOfProducts().size());
    }

    @Test
    public void shouldIncreaseProductQuantity() {
        warehouseSectorService.addProductWithQuantityBySectorId(product, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(product, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(product2, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);

        Integer actualProductQuantity = warehouseSector.getNotReservedAmountOfProducts().get(product.getId()).getQuantity();
        Integer expectedProductQuantity = 30;

        Assert.assertEquals(2, warehouseSector.getNotReservedAmountOfProducts().size());
        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }

    @Test
    public void shouldReturnFalseWhenIncreaseProductQuantityOverMaxAmountOfWarehouseSector() {
        Integer overMaxAmountOfWarehouseSector = 100;

        boolean result = warehouseSectorService.addProductWithQuantityBySectorId(product, overMaxAmountOfWarehouseSector, WAREHOUSE_SECTOR_ID);

        Assert.assertFalse(result);
    }

    @Test
    public void shouldDecreaseProductQuantity() {
        AmountOfProduct amountOfProduct = new AmountOfProduct(product.getId(), 30);

        warehouseSectorService.addProductWithQuantityBySectorId(product, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(product, PRODUCT_QUANTITY, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.removeAmountOfProductBySectorId(amountOfProduct, WAREHOUSE_SECTOR_ID);

        Integer actualProductQuantity = warehouseSector.getQuantityOfNotReservedProductByIdIfExist(product.getId());
        Integer expectedProductQuantity = 0;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
    }

    @Test
    public void shouldReserveAmountOfProduct() {
        AmountOfProduct amountOfProduct = new AmountOfProduct(product.getId(), 15);
        AmountOfProduct amountOfProduct2 = new AmountOfProduct(product2.getId(), 20);

        warehouseSectorService.addProductWithQuantityBySectorId(product, 20, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.addProductWithQuantityBySectorId(product2, 20, WAREHOUSE_SECTOR_ID);

        warehouseSectorService.reserveAmountOfProduct(amountOfProduct);
        warehouseSectorService.reserveAmountOfProduct(amountOfProduct2);

        Integer actualProductQuantity = warehouseSector.getCurrentAmountOfProducts();
        Integer actualProductQuantity1 = warehouseSector.getReservedAmountOfProducts().get(product.getId()).getQuantity();
        Integer actualProductQuantity2 = warehouseSector.getReservedAmountOfProducts().get(product2.getId()).getQuantity();
        Integer expectedProductQuantity = 40;
        Integer expectedProductQuantity1 = 15;
        Integer expectedProductQuantity2 = 20;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
        Assert.assertEquals(expectedProductQuantity1, actualProductQuantity1);
        Assert.assertEquals(expectedProductQuantity2, actualProductQuantity2);
    }

    @Test
    public void shouldUnReserveAmountOfProduct() {
        AmountOfProduct amountOfProduct = new AmountOfProduct(product.getId(), 15);
        AmountOfProduct amountOfProduct2 = new AmountOfProduct(product.getId(), 10);

        warehouseSectorService.addProductWithQuantityBySectorId(product, 20, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.reserveAmountOfProduct(amountOfProduct);
        warehouseSectorService.unReserveAmountOfProduct(amountOfProduct2);

        Integer actualProductQuantity = warehouseSector.getQuantityOfNotReservedProductByIdIfExist(product.getId());
        Integer actualProductQuantity1 = warehouseSector.getQuantityOfReservedProductByIdIfExist(product.getId());
        Integer expectedProductQuantity = 15;
        Integer expectedProductQuantity1 = 5;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantity);
        Assert.assertEquals(expectedProductQuantity1, actualProductQuantity1);
    }

    @Test
    public void shouldRemoveReservedAmountOfProduct() {
        AmountOfProduct reserveAmountOfProduct = new AmountOfProduct(product.getId(), 15);
        AmountOfProduct removeReservedAmountOfProduct = new AmountOfProduct(product.getId(), 15);

        warehouseSectorService.addProductWithQuantityBySectorId(product, 20, WAREHOUSE_SECTOR_ID);
        warehouseSectorService.reserveAmountOfProduct(reserveAmountOfProduct);
        warehouseSectorService.removeReservedAmountOfProduct(removeReservedAmountOfProduct);

        Integer actualProductQuantityInAmountOfProducts = warehouseSector.getNotReservedAmountOfProducts().get(product.getId()).getQuantity();
        Boolean isContaining = warehouseSector.getReservedAmountOfProducts().containsKey(product.getId());
        Integer currentAmount = warehouseSector.getCurrentAmountOfProducts();

        Integer expectedProductQuantity = 5;
        Integer expectedCurrentAmount = 5;

        Assert.assertEquals(expectedProductQuantity, actualProductQuantityInAmountOfProducts);
        Assert.assertFalse(isContaining);
        Assert.assertEquals(expectedCurrentAmount, currentAmount);
    }
}
