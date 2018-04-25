package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<SaleOrder, Long> {

}
