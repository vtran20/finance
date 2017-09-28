package com.easysoft.finance.repository;

import com.easysoft.finance.domain.StockOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockOrderRepository extends CrudRepository<StockOrder, Long> {
    @Query("select c from StockOrder c where (c.sent is null or c.sent <> 'Y') and c.buyNum > 0 and (c.sellNum is null or c.buyNum > c.sellNum)")
    List<StockOrder> findActiveOrders();

}
