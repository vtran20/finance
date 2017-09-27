package com.easysoft.finance.repository;

import com.easysoft.finance.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    //@Query("select c from Order c where  c.buyNum > c.sellNum")
    //List<Order> findActiveOrders();

}
