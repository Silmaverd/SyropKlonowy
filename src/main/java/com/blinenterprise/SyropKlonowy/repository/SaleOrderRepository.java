package com.blinenterprise.SyropKlonowy.repository;

import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrder;
import com.blinenterprise.SyropKlonowy.domain.SaleOrder.SaleOrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Repository
public interface SaleOrderRepository extends CrudRepository<SaleOrder, Long> {

    List<SaleOrder> findAllByClientId(Long clientId);

    @Query("select max(s.totalPrice) from SaleOrder s where s.clientId=:clientId")
    BigDecimal findMaxPriceInClientOrders(@Param("clientId") Long clientId);

    @Query("select min(s.totalPrice) from SaleOrder s where s.clientId=:clientId")
    BigDecimal findMinPriceInClientOrders(@Param("clientId") Long clientId);

    @Query("select max(p.price) from SaleOrder s join s.productsToOrder aop, Product p where aop.productId=p.id and s.clientId=:clientId")
    BigDecimal findMaxPriceOfProductInClientOrders(@Param("clientId") Long clientId);

    @Query("select avg(p.price) from SaleOrder s join s.productsToOrder aop, Product p where aop.productId=p.id and s.clientId=:clientId")
    BigDecimal findAveragePriceOfProductInClientOrders(@Param("clientId") Long clientId);

    @Query("select p.id, sum(aop.quantity) as sq from SaleOrder s join s.productsToOrder aop, Product p where aop.productId=p.id and s.clientId=:clientId group by p.id order by sq desc")
    List<Object[]> findProductIdFromAllOrdersWithSumOfQuantity(@Param("clientId") Long clientId);

    @Query("select aop.productId, count(aop.productId) from SaleOrder s join s.productsToOrder aop where s.id in" +
            "(select s.id from SaleOrder s join s.productsToOrder aop where aop.productId=:productId)" +
            "and aop.productId<>:productId group by aop.productId order by count(aop.productId) desc")
    List<Object[]> findFrequentlyBoughtTogether(@Param("productId") Long productId);

    @Query(value = "select top 3 aop.product_Id, count(aop.product_Id) as cp from Sale_Order s, sale_order_products_to_order sp, Amount_Of_Product aop " +
            "where  s.id = sp.SALE_ORDER_ID and aop.id = sp.products_to_order_id and s.date_of_order > DATEADD (dd, -7, GETDATE()) " +
            "group by aop.product_Id order by cp desc", nativeQuery = true)
    List<Object[]> findFrequentlyBoughtInLastWeek();

    @Query(value = "select top :productAmountToLoad aop.product_Id, count(aop.product_Id) as cp from Sale_Order s, sale_order_products_to_order sp, Amount_Of_Product aop, Client c " +
            "where  s.id = sp.SALE_ORDER_ID and aop.id = sp.products_to_order_id and s.client_Id=c.id " +
            "and s.date_of_order > DATEADD (dd, -7, GETDATE()) and c.enterprise_Type = :enterpriseType " +
            "group by aop.product_Id order by cp desc", nativeQuery = true)
    List<Object[]> findFrequentlyBoughtInLastWeek(@Param("enterpriseType") String enterpriseType, @Param("productAmountToLoad") Integer productAmountToLoad);

    @Query(value = "select aop.product_Id, sum(aop.product_Id) as cp from Sale_Order s, sale_order_products_to_order sp, Amount_Of_Product aop " +
            "where  s.id = sp.SALE_ORDER_ID and aop.id = sp.products_to_order_id " +
            "group by aop.product_Id order by cp desc", nativeQuery = true)
    List<Object[]> findBoughtProductsSum();

    @Query(value = "select sum(s.total_price) as cp from Sale_Order s where  s.date_of_order between :startDate and :endDate", nativeQuery = true)
    BigDecimal findIncomeFromOrders(@Param("startDate")Date startDate, @Param("endDate") Date endDate);
    public List<SaleOrder> findAllByDateOfOrderAfter(Date date);

    public List<SaleOrder> findAllByDateOfOrderBetween(Date fromDate, Date toDate);

    List<SaleOrder> findAllBySaleOrderStatus(SaleOrderStatus saleOrderStatus);
}
