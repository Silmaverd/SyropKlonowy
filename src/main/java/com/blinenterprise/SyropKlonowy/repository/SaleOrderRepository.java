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

    @Query("select p.id, sum(aop.quantity) as sq from SaleOrder s join s.amountsOfProducts aop, Product p where aop.productId=p.id and s.clientId=:clientId group by p.id order by sq desc")
    List<Object[]> findProductIdFromAllOrdersWithSumOfQuantity(@Param("clientId") Long clientId);

    @Query("select aop.productId, count(aop.productId) from SaleOrder s join s.amountsOfProducts aop where s.id in" +
            "(select s.id from SaleOrder s join s.amountsOfProducts aop where aop.productId=:productId)" +
            "and aop.productId<>:productId group by aop.productId order by count(aop.productId) desc")
    List<Object[]> findFrequentlyBoughtTogether(@Param("productId") Long productId);

    @Query(value = "select top 3 aop.product_Id, count(aop.product_Id) as cp from Sale_Order s, sale_order_amounts_of_products sa, Amount_Of_Product aop " +
            "where  s.id = sa.SALE_ORDER_ID and aop.id = sa.AMOUNTS_OF_PRODUCTS_ID and s.date_of_order > DATEADD (dd, -7, GETDATE()) " +
            "group by aop.product_Id order by cp desc", nativeQuery = true)
    List<Object[]> findFrequentlyBoughtInLastWeek();

    @Query(value = "select top :productAmountToLoad aop.product_Id, count(aop.product_Id) as cp from Sale_Order s, sale_order_amounts_of_products sa, Amount_Of_Product aop, Client c " +
            "where  s.id = sa.SALE_ORDER_ID and aop.id = sa.AMOUNTS_OF_PRODUCTS_ID and s.client_Id=c.id " +
            "and s.date_of_order > DATEADD (dd, -7, GETDATE()) and c.enterprise_Type = :enterpriseType " +
            "group by aop.product_Id order by cp desc", nativeQuery = true)
    List<Object[]> findFrequentlyBoughtInLastWeek(@Param("enterpriseType") String enterpriseType, @Param("productAmountToLoad") Integer productAmountToLoad);


}
