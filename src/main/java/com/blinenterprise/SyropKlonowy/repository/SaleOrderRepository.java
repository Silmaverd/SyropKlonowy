package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SaleOrderRepository extends CrudRepository<SaleOrder, Long> {

    List<SaleOrder> findAllByClientId(Long clientId);

    @Query("select max(s.totalPrice) from SaleOrder s where s.clientId=:clientId")
    BigDecimal findMaxPriceInClientOrders(@Param("clientId") Long clientId);

    @Query("select min(s.totalPrice) from SaleOrder s where s.clientId=:clientId")
    BigDecimal findMinPriceInClientOrders(@Param("clientId") Long clientId);

    @Query("select max(p.price) from SaleOrder s join s.amountsOfProducts aop, Product p where aop.productId=p.id and s.clientId=:clientId")
    BigDecimal findMaxPriceOfProductInClientOrders(@Param("clientId") Long clientId);

    @Query("select avg(p.price) from SaleOrder s join s.amountsOfProducts aop, Product p where aop.productId=p.id and s.clientId=:clientId")
    BigDecimal findAveragePriceOfProductInClientOrders(@Param("clientId") Long clientId);

    @Query("select p.id, sum(aop.quantity) from SaleOrder s join s.amountsOfProducts aop, Product p where aop.productId=p.id and s.clientId=:clientId group by p.id")
    List<Object[]> findProductIdFromAllOrdersWithSumOfQuantity(@Param("clientId") Long clientId);

    @Query("select aop.productId, count(aop.productId) from SaleOrder s join s.amountsOfProducts aop where s.id in" +
            "(select s.id from SaleOrder s join s.amountsOfProducts aop where aop.productId=:productId)" +
            "and aop.productId<>:productId group by aop.productId order by count(aop.productId) desc")
    List<Object[]> findFrequentlyBoughtTogether(@Param("productId") Long productId);


}
