package com.blinenterprise.SyropKlonowy.service;

import com.blinenterprise.SyropKlonowy.domain.Client.Client;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import com.blinenterprise.SyropKlonowy.repository.ClientRepository;
import com.blinenterprise.SyropKlonowy.repository.SaleOrderRepository;
import com.blinenterprise.SyropKlonowy.repository.WarehouseSectorRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class StatusService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private WarehouseSectorRepository warehouseSectorRepository;

    @Autowired
    private ClientRepository clientRepository;

    public BigDecimal getTotalIncomeFromOrdersSince(Date since) {
        return new BigDecimal(saleOrderRepository.findAllByDateOfOrderAfter(since)
                .stream()
                .filter(saleOrder -> saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.SENT))
                .mapToInt(saleOrder -> saleOrder.getTotalPrice().intValue())
                .sum());
    }

    public BigDecimal getNumberOfProductsSoldSince(Date since) {
        return new BigDecimal(saleOrderRepository.findAllByDateOfOrderAfter(since)
                .stream()
                .filter(saleOrder -> saleOrder.getSaleOrderStatus().equals(SaleOrderStatus.SENT))
                .mapToInt(saleOrder ->
                        saleOrder.getProductsToOrder().stream().mapToInt(amountOfProduct -> amountOfProduct.getQuantity()).sum()
                )
                .sum());
    }

    public Integer getAmountOfWarehouseSectors(){
        return Lists.newArrayList(warehouseSectorRepository.findAll()).size();
    }

    public List<Client> getClientsActiveSice(Date date) {
        return clientRepository.findAllHavingOrdersSince(date);
    }
}
