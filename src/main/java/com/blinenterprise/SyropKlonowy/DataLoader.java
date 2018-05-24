package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.Client.Address;
import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.Client.Enterprise;
import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product.Category;
import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.WarehouseSector.WarehouseSector;
import com.blinenterprise.SyropKlonowy.repository.*;
import com.blinenterprise.SyropKlonowy.service.SaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Deprecated
@Component
public class DataLoader {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WarehouseSectorRepository warehouseSectorRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ProductWithQuantityRepository productWithQuantityRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    SaleOrderService saleOrderService;

    private void loadProductsWithQuantity() {
        WarehouseSector warehouseSector1 = new WarehouseSector("MAIN1", 50);
        WarehouseSector warehouseSector2 = new WarehouseSector("MAIN2", 50);
        WarehouseSector warehouseSector3 = new WarehouseSector("MAIN3", 50);
        warehouseSectorRepository.save(warehouseSector1);
        warehouseSectorRepository.save(warehouseSector2);
        warehouseSectorRepository.save(warehouseSector3);

        List<Product> products = Arrays.asList(
                new Product("PC1", new BigDecimal(15556), Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC", "X324"),
                new Product("Laptop1", new BigDecimal(356342), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1", "XEWE"),
                new Product("Laptop3", new BigDecimal(200030), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2", "AVE32"),
                new Product("Smarphone", new BigDecimal(80099), Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop", "ADAG21"),
                new Product("Speaker1", new BigDecimal(512), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1", "23A5"),
                new Product("Speaker2", new BigDecimal(1012), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2", "135DGG2")
        );
        productRepository.saveAll(products);

        List<AmountOfProduct> amountOfProducts = new ArrayList<>();
        products.forEach(product -> amountOfProducts.add(new AmountOfProduct(product.getId(), 20)));

        amountOfProducts.forEach(warehouseSector1::addAmountOfProduct);
        amountOfProducts.forEach(warehouseSector2::addAmountOfProduct);
        amountOfProducts.forEach(warehouseSector3::addAmountOfProduct);
        warehouseSectorRepository.save(warehouseSector1);
        warehouseSectorRepository.save(warehouseSector2);
        warehouseSectorRepository.save(warehouseSector3);
    }

    private void loadOneProductWithDifferentQuantities() {
        WarehouseSector warehouseSector1 = warehouseSectorRepository.findByName("MAIN1").orElseThrow(IllegalArgumentException::new);
        WarehouseSector warehouseSector2 = warehouseSectorRepository.findByName("MAIN2").orElseThrow(IllegalArgumentException::new);
        WarehouseSector warehouseSector3 = warehouseSectorRepository.findByName("MAIN3").orElseThrow(IllegalArgumentException::new);

        Product productToOrder = new Product("DSADAD", new BigDecimal(10.12), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2", "1111");
        productRepository.save(productToOrder);
        AmountOfProduct aop1 = new AmountOfProduct(productToOrder.getId(), 10);
        AmountOfProduct aop2 = new AmountOfProduct(productToOrder.getId(), 5);
        AmountOfProduct aop3 = new AmountOfProduct(productToOrder.getId(), 8);
        warehouseSector1.addAmountOfProduct(aop3);
        warehouseSector2.addAmountOfProduct(aop2);
        warehouseSector3.addAmountOfProduct(aop1);
        warehouseSectorRepository.save(warehouseSector1);
        warehouseSectorRepository.save(warehouseSector2);
        warehouseSectorRepository.save(warehouseSector3);
    }

    private void loadDeliveries() {

        List<Product> products = Arrays.asList(
                new Product("phone", new BigDecimal(10012), Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone", "2323"),
                new Product("audio", new BigDecimal(5033), Category.AUDIO, Date.valueOf(LocalDate.now().minusWeeks(3)), "audio", "2325425"),
                new Product("speaker", new BigDecimal(3023), Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(2)), "speaker", "SDAD22"),
                new Product("computer", new BigDecimal(5033), Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(7)), "computer", "322AAA")
        );
        productRepository.saveAll(products);


        ArrayList<ProductWithQuantity> products1 = Lists.newArrayList(
                new ProductWithQuantity(products.get(0), 10),
                new ProductWithQuantity(products.get(1), 5)
        );
        productWithQuantityRepository.saveAll(products1);

        ArrayList<ProductWithQuantity> products2 = Lists.newArrayList(
                new ProductWithQuantity(products.get(2), 50),
                new ProductWithQuantity(products.get(3), 15)
        );
        productWithQuantityRepository.saveAll(products2);

        Long warehouseId = warehouseSectorRepository.findByName("MAIN1").get().getId();
        log.debug("Main warehouse id: " + warehouseId);
        List<Delivery> deliveries = Arrays.asList(
                new Delivery(products1),
                new Delivery(products2)
        );
        deliveryRepository.saveAll(deliveries);
    }

    private void loadClientsAndAddresses() {
        List<Address> addresses = Arrays.asList(
                new Address("Koryznowa", "1", "Lublin", 2424),
                new Address("Czeladników", "55", "Krakow", 2235),
                new Address("Wiejska", "3", "Warszawa", 23142),
                new Address("Niewiadomych", "4", "Poznań", 132)
        );
        addressRepository.saveAll(addresses);

        List<Client> clients = Arrays.asList(
                new Client("Klient1", "Company1", true, addresses.get(0), Enterprise.LARGE_ENTERPRISE),
                new Client("Klient2", "Company2", false, addresses.get(1), Enterprise.SMALL_ENTERPRISE),
                new Client("Klient3", "Company3", false, addresses.get(2), Enterprise.PRIVATE_PERSON),
                new Client("Klient4", "Company4", true, addresses.get(3), Enterprise.LARGE_ENTERPRISE)
        );
        clientRepository.saveAll(clients);
    }

    private void loadSaleOrders() {
        Long productId1 = productRepository.findByCode("X324").get().getId();
        Long productId2 = productRepository.findByCode("AVE32").get().getId();
        Long productId3 = productRepository.findByCode("135DGG2").get().getId();
        Long productId4 = productRepository.findByCode("2325425").get().getId();

        List<AmountOfProduct> amountsOfProducts1 = Arrays.asList(
                new AmountOfProduct(productId1, 6),
                new AmountOfProduct(productId2, 5),
                new AmountOfProduct(productId3, 20)
        );

        List<AmountOfProduct> amountsOfProducts2 = Arrays.asList(
                new AmountOfProduct(productId2, 10),
                new AmountOfProduct(productId3, 20),
                new AmountOfProduct(productId4, 2)
        );

        List<AmountOfProduct> amountsOfProducts3 = Arrays.asList(
                new AmountOfProduct(productId1, 2),
                new AmountOfProduct(productId3, 20),
                new AmountOfProduct(productId4, 8)
        );

        List<AmountOfProduct> amountsOfProducts4 = Arrays.asList(
                new AmountOfProduct(productId2, 2),
                new AmountOfProduct(productId4, 11)
        );


        List<Client> clientsByName1 = Lists.newArrayList(clientRepository.findAllByName("Klient1"));
        Long clientId1 = clientsByName1.get(0).getId();
        List<Client> clientsByName2 = Lists.newArrayList(clientRepository.findAllByName("Klient3"));
        Long clientId2 = clientsByName2.get(0).getId();
        List<Client> clientsByName3 = Lists.newArrayList(clientRepository.findAllByName("Klient2"));
        Long clientId3 = clientsByName3.get(0).getId();


        List<SaleOrder> saleOrders = Arrays.asList(
                new SaleOrder(clientId1, Date.valueOf(LocalDate.now()), amountsOfProducts3, new BigDecimal(300), SaleOrderStatus.NEW),
                new SaleOrder(clientId2, Date.valueOf(LocalDate.now()), amountsOfProducts1, new BigDecimal(400), SaleOrderStatus.PAID),
                new SaleOrder(clientId2, Date.valueOf(LocalDate.now().minusWeeks(2)), amountsOfProducts2, new BigDecimal(500), SaleOrderStatus.PAID),
                new SaleOrder(clientId3, Date.valueOf(LocalDate.now().minusDays(12)), amountsOfProducts4, new BigDecimal(980), SaleOrderStatus.SENT)
        );
        saleOrderRepository.saveAll(saleOrders);
    }

    public void loadTestDataBase() {
        loadProductsWithQuantity();
        loadOneProductWithDifferentQuantities();
        loadDeliveries();
        loadClientsAndAddresses();
        loadSaleOrders();
    }
}
