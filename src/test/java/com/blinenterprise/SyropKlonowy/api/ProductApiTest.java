package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.service.ProductService;
import com.blinenterprise.SyropKlonowy.view.ProductView;
import com.blinenterprise.SyropKlonowy.view.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductApiTest {

    @InjectMocks
    private ProductApi productApi;

    @Mock
    private ProductService productService;

    @Test
    public void getAllProductTest() {
        List<Product> expected = Arrays.asList(
                new Product("phone", new BigDecimal(100122), Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone1", "223"),
                new Product("phone", new BigDecimal(10022), Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone2", "2323"));
        Mockito.doAnswer(a -> expected).when(productService).findAllByName(ArgumentMatchers.any());

        Response<ProductView> actualResponse = productApi.getProductByName("phone");

        Assert.assertTrue(actualResponse.getPayload().stream().anyMatch(productView -> productView.getDescription().equals(expected.get(0).getDescription())));
        Assert.assertTrue(actualResponse.getPayload().stream().anyMatch(productView -> productView.getDescription().equals(expected.get(1).getDescription())));
    }


    @Test
    public void getProductByNameTest(){
        Product expected = new Product("phone", new BigDecimal(100122), Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone", "2323");
        Mockito.doAnswer(a -> Arrays.asList(expected)).when(productService).findAllByName(ArgumentMatchers.any());

        Response<ProductView> actualResponse = productApi.getProductByName("phone");

        Assert.assertEquals(expected.getName(), actualResponse.getPayload().get(0).getName());
    }

    @Test
    public void getProductByIdTest(){
        Product expected = new Product(1L, "phone", new BigDecimal(100122), Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone", "2323");
        Mockito.doAnswer(a -> Optional.of(expected)).when(productService).findById(ArgumentMatchers.any());

        Response<ProductView> actualResponse = productApi.getProductById(1L);

        Assert.assertEquals(expected.getName(), actualResponse.getPayload().get(0).getName());
    }
}
