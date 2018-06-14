package com.blinenterprise.SyropKlonowy.api;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import com.blinenterprise.SyropKlonowy.view.Response;
import com.blinenterprise.SyropKlonowy.view.SaleOrderView;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SaleOrderApiTest {

    @InjectMocks
    private SaleOrderApi saleOrderApi;

    @Mock
    private SaleOrderService saleOrderService;

    @Test
    public void getOrderByIdTest() {
        SaleOrder expected = new SaleOrder(2L, 3L, Date.valueOf(LocalDate.now()), new ArrayList<AmountOfProduct>(), new BigDecimal(100122), SaleOrderStatus.NEW);
        Mockito.doAnswer(a -> expected).when(saleOrderService).findById(ArgumentMatchers.any());

        Response<SaleOrderView> actualResponse = saleOrderApi.getOrderById(2L);

        Assert.assertEquals(expected.getClientId(), actualResponse.getPayload().get(0).getClientId());
    }

    @Test
    public void getAllOrdersTest() {
        List<SaleOrder> expected = Arrays.asList(
                new SaleOrder(2L, 2L, Date.valueOf(LocalDate.now()), new ArrayList<AmountOfProduct>(), new BigDecimal(100122), SaleOrderStatus.NEW),
                new SaleOrder(3L, 3L, Date.valueOf(LocalDate.now()), new ArrayList<AmountOfProduct>(), new BigDecimal(122), SaleOrderStatus.NEW));
        Mockito.doAnswer(a -> expected).when(saleOrderService).findAll();

        Response<SaleOrderView> actualResponse = saleOrderApi.getAllOrders();

        Assert.assertTrue(actualResponse.getPayload().stream().anyMatch(saleOrderView -> saleOrderView.getId().equals(expected.get(0).getId())));
        Assert.assertTrue(actualResponse.getPayload().stream().anyMatch(saleOrderView -> saleOrderView.getId().equals(expected.get(1).getId())));
    }

    @Test
    public void payOrderById() {
        Mockito.doAnswer(a -> true).when(saleOrderService).payById(ArgumentMatchers.any());

        Response<SaleOrderView> actualResponse = saleOrderApi.payOrderById(2L);

        Assert.assertTrue(!actualResponse.getErrorMessage().isPresent());
    }

    @Test
    public void sendById() {
        Mockito.doAnswer(a -> true).when(saleOrderService).sendById(ArgumentMatchers.any());

        Response<SaleOrderView> actualResponse = saleOrderApi.sendOrderById(2L);

        Assert.assertTrue(!actualResponse.getErrorMessage().isPresent());
    }

    @Test
    public void getTemporaryOrderByClientIdTest() {
        SaleOrder expected = new SaleOrder(1L, Date.valueOf(LocalDate.now()), new ArrayList<AmountOfProduct>(), new BigDecimal(100122), SaleOrderStatus.NEW);
        Mockito.doAnswer(a -> Optional.of(expected)).when(saleOrderService).findTemporaryOrderOfClient(ArgumentMatchers.any());

        Response<SaleOrderView> actualResponse = saleOrderApi.getTemporaryOrderByClientId(1L);

        Assert.assertEquals(expected.getClientId(), actualResponse.getPayload().get(0).getClientId());
    }
}
