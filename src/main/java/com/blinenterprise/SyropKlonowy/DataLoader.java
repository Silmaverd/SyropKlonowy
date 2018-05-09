package com.blinenterprise.SyropKlonowy;

import com.blinenterprise.SyropKlonowy.domain.AmountOfProduct;
import com.blinenterprise.SyropKlonowy.domain.Category;
import com.blinenterprise.SyropKlonowy.domain.Delivery.Delivery;
import com.blinenterprise.SyropKlonowy.domain.Delivery.ProductWithQuantity;
import com.blinenterprise.SyropKlonowy.domain.Product;
import com.blinenterprise.SyropKlonowy.domain.Warehouse;
import com.blinenterprise.SyropKlonowy.repository.DeliveryRepository;
import com.blinenterprise.SyropKlonowy.repository.ProductRepository;
import com.blinenterprise.SyropKlonowy.repository.ProductWithQuantityRepository;
import com.blinenterprise.SyropKlonowy.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private WarehouseRepository warehouseRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ProductWithQuantityRepository productWithQuantityRepository;

    public void loadData() {
        Warehouse warehouse = new Warehouse("MAIN");
        warehouseRepository.save(warehouse);

        List<Product> products = Arrays.asList(
                new Product("PC1", "155.56", Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(5)), "universal PC", "X324"),
                new Product("Laptop1", "3563.42", Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusYears(4)), "laptop 1", "XEWE"),
                new Product("Laptop3", "2000.30", Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusDays(3)), "laptop 2", "AVE32"),
                new Product("Smarphone", "800.99", Category.COMPUTER_LAPTOP, Date.valueOf(LocalDate.now().minusWeeks(1)), "laptop", "ADAG21"),
                new Product("Speaker1", "5.12", Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 1", "23A5"),
                new Product("Speaker2", "10.12", Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(1)), "speaker 2", "135DGG2")
        );
        productRepository.saveAll(products);

        List<AmountOfProduct> amountOfProducts = new ArrayList<>();
        products.forEach(product -> amountOfProducts.add(new AmountOfProduct(product.getId(), 20)));

        amountOfProducts.forEach(warehouse::addAmountOfProduct);
        warehouseRepository.save(warehouse);
    }


    public void loadDeliveries() {

        List<Product> products = Arrays.asList(
                new Product("phone", "100.12", Category.PHONE, Date.valueOf(LocalDate.now().minusWeeks(1)), "phone", "2323"),
                new Product("audio", "50.33", Category.AUDIO, Date.valueOf(LocalDate.now().minusWeeks(3)), "audio", "2325425"),
                new Product("speaker", "30.23", Category.SPEAKER, Date.valueOf(LocalDate.now().minusWeeks(2)), "speaker", "SDAD22"),
                new Product("computer", "50.33", Category.COMPUTER_PC, Date.valueOf(LocalDate.now().minusWeeks(7)), "computer", "322AAA")
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

        Long warehouseId = warehouseRepository.findByName("MAIN").get().getId();
        log.debug("Main warehouse id: "+warehouseId);
        List<Delivery> deliveries = Arrays.asList(
                new Delivery(products1, warehouseId),
                new Delivery(products2, warehouseId)
        );
        deliveryRepository.saveAll(deliveries);
    }
}
